package com.vitorsousa.stallfit.data.local

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Runs the real [MIGRATION_1_2] / [MIGRATION_2_3] SQL against the schema snapshots exported to
 * `app/schemas/` (wired as an androidTest asset dir in app/build.gradle.kts), instead of relying
 * on Room's destructive fallback — this is what actually caught the set_entries cascade-delete
 * bug during the migration rewrite, and guards against future schema bumps repeating it.
 */
@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val dbName = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        StallFitDatabase::class.java
    )

    @Test
    fun migrate1To2_preservesExercisesSessionsAndLoggedSets() {
        helper.createDatabase(dbName, 1).apply {
            execSQL("INSERT INTO exercises (id, name, muscleGroup, isCustom) VALUES (1, 'Supino', 'CHEST', 0)")
            execSQL("INSERT INTO workout_sessions (id, name, startedAt, finishedAt) VALUES (1, 'Treino A', 1000, 2000)")
            execSQL(
                "INSERT INTO set_entries (id, sessionId, exerciseId, setNumber, reps, weightKg, loggedAt) " +
                    "VALUES (1, 1, 1, 1, 10, 80.0, 1500)"
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(dbName, 2, true, MIGRATION_1_2)

        db.query("SELECT equipment FROM exercises WHERE id = 1").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("NONE", cursor.getString(0))
        }
        db.query("SELECT templateId FROM workout_sessions WHERE id = 1").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertTrue(cursor.isNull(0))
        }
        // The set logged before the migration must survive the workout_sessions rebuild —
        // this is exactly the row an implicit cascade delete would have wiped.
        db.query("SELECT weightKg, reps FROM set_entries WHERE id = 1").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(80.0, cursor.getDouble(0), 0.001)
            assertEquals(10, cursor.getInt(1))
        }
    }

    @Test
    fun migrate2To3_movesLegacyProfileMeasurementsIntoHistory() {
        helper.createDatabase(dbName, 2).apply {
            execSQL(
                "INSERT INTO user_profile " +
                    "(id, ageYears, weightKg, heightCm, sex, activityLevel, goal, armCm, chestCm, hipCm, thighCm, calfCm) " +
                    "VALUES (1, 30, 80.0, 180.0, 'MALE', 'MODERATE', 'MAINTENANCE', 35.0, 100.0, 95.0, 55.0, 38.0)"
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(dbName, 3, true, MIGRATION_2_3)

        db.query("SELECT name, ageYears, sex FROM user_profile WHERE id = 1").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("", cursor.getString(0))
            assertEquals(30, cursor.getInt(1))
            assertEquals("MALE", cursor.getString(2))
        }
        // weightKg/heightCm/chestCm/hipCm from the old single-row profile must survive as the
        // first body_measurements entry instead of being silently dropped.
        db.query("SELECT weightKg, heightCm, chestCm, hipCm FROM body_measurements").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(80.0, cursor.getDouble(0), 0.001)
            assertEquals(180.0, cursor.getDouble(1), 0.001)
            assertEquals(100.0, cursor.getDouble(2), 0.001)
            assertEquals(95.0, cursor.getDouble(3), 0.001)
        }
    }

    @Test
    fun migrate3To4_addsReferenceWeightAndNotesColumnsAsNullable() {
        helper.createDatabase(dbName, 3).apply {
            execSQL("INSERT INTO exercises (id, name, muscleGroup, equipment, isCustom) VALUES (1, 'Supino', 'CHEST', 'BARBELL', 0)")
            execSQL("INSERT INTO workout_templates (id, title, createdAt) VALUES (1, 'A: Peito e Tríceps', 1000)")
            execSQL(
                "INSERT INTO template_exercises " +
                    "(id, templateId, exerciseId, orderIndex, sets, repRangeMin, repRangeMax, restSeconds, intensity) " +
                    "VALUES (1, 1, 1, 0, 4, 8, 12, 90, 'MODERADA')"
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(dbName, 4, true, MIGRATION_3_4)

        // Pre-existing rows must survive the ADD COLUMN with the new fields defaulting to NULL.
        db.query("SELECT notes FROM workout_templates WHERE id = 1").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertTrue(cursor.isNull(0))
        }
        db.query("SELECT referenceWeightKg FROM template_exercises WHERE id = 1").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertTrue(cursor.isNull(0))
        }
    }

    @Test
    fun migrateAll_1To4_runsCleanOnAnEmptyDatabase() {
        helper.createDatabase(dbName, 1).apply { close() }

        helper.runMigrationsAndValidate(dbName, 4, true, MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
    }
}
