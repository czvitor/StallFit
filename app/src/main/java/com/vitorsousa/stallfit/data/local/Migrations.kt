package com.vitorsousa.stallfit.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Hand-written migrations, derived column-by-column from the exported schema snapshots in
 * `app/schemas/`. Every future version bump must add a new Migration here — never fall back to
 * destructive migration for a forward upgrade, or real user data gets wiped on update.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE exercises ADD COLUMN equipment TEXT NOT NULL DEFAULT 'NONE'")

        // SQLite can't ALTER TABLE ADD a FOREIGN KEY, so workout_sessions is rebuilt to add the
        // new templateId column — id/name/startedAt/finishedAt are preserved as-is.
        //
        // set_entries has an ON DELETE CASCADE foreign key pointing at workout_sessions. Room
        // enables `PRAGMA foreign_keys=ON`, and SQLite performs an *implicit DELETE* on a table
        // before a DROP TABLE whenever that table is referenced by another table's foreign key —
        // so a bare `DROP TABLE workout_sessions` here would cascade-wipe every row in set_entries
        // (all logged sets) before the table is even rebuilt. To avoid that, set_entries is copied
        // out to an unconstrained holding table and dropped *first*, so workout_sessions is no
        // longer a referenced parent by the time it's dropped and rebuilt; set_entries is then
        // recreated (unchanged shape) pointing at the new workout_sessions table.
        db.execSQL(
            """
            CREATE TABLE set_entries_backup (
                id INTEGER NOT NULL,
                sessionId INTEGER NOT NULL,
                exerciseId INTEGER NOT NULL,
                setNumber INTEGER NOT NULL,
                reps INTEGER NOT NULL,
                weightKg REAL NOT NULL,
                loggedAt INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO set_entries_backup (id, sessionId, exerciseId, setNumber, reps, weightKg, loggedAt)
            SELECT id, sessionId, exerciseId, setNumber, reps, weightKg, loggedAt FROM set_entries
            """.trimIndent()
        )
        db.execSQL("DROP TABLE set_entries")

        db.execSQL(
            """
            CREATE TABLE workout_sessions_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                startedAt INTEGER NOT NULL,
                finishedAt INTEGER,
                templateId INTEGER,
                FOREIGN KEY(templateId) REFERENCES workout_templates(id) ON UPDATE NO ACTION ON DELETE SET NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO workout_sessions_new (id, name, startedAt, finishedAt, templateId)
            SELECT id, name, startedAt, finishedAt, NULL FROM workout_sessions
            """.trimIndent()
        )
        db.execSQL("DROP TABLE workout_sessions")
        db.execSQL("ALTER TABLE workout_sessions_new RENAME TO workout_sessions")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_workout_sessions_templateId ON workout_sessions (templateId)")

        db.execSQL(
            """
            CREATE TABLE set_entries (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                sessionId INTEGER NOT NULL,
                exerciseId INTEGER NOT NULL,
                setNumber INTEGER NOT NULL,
                reps INTEGER NOT NULL,
                weightKg REAL NOT NULL,
                loggedAt INTEGER NOT NULL,
                FOREIGN KEY(sessionId) REFERENCES workout_sessions(id) ON UPDATE NO ACTION ON DELETE CASCADE,
                FOREIGN KEY(exerciseId) REFERENCES exercises(id) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO set_entries (id, sessionId, exerciseId, setNumber, reps, weightKg, loggedAt)
            SELECT id, sessionId, exerciseId, setNumber, reps, weightKg, loggedAt FROM set_entries_backup
            """.trimIndent()
        )
        db.execSQL("DROP TABLE set_entries_backup")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_set_entries_sessionId ON set_entries (sessionId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_set_entries_exerciseId ON set_entries (exerciseId)")

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS user_profile (
                id INTEGER NOT NULL,
                ageYears INTEGER NOT NULL,
                weightKg REAL NOT NULL,
                heightCm REAL NOT NULL,
                sex TEXT NOT NULL,
                activityLevel TEXT NOT NULL,
                goal TEXT NOT NULL,
                armCm REAL,
                chestCm REAL,
                hipCm REAL,
                thighCm REAL,
                calfCm REAL,
                PRIMARY KEY(id)
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS workout_templates (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                createdAt INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS template_exercises (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                templateId INTEGER NOT NULL,
                exerciseId INTEGER NOT NULL,
                orderIndex INTEGER NOT NULL,
                sets INTEGER NOT NULL,
                repRangeMin INTEGER NOT NULL,
                repRangeMax INTEGER NOT NULL,
                restSeconds INTEGER NOT NULL,
                intensity TEXT NOT NULL,
                FOREIGN KEY(templateId) REFERENCES workout_templates(id) ON UPDATE NO ACTION ON DELETE CASCADE,
                FOREIGN KEY(exerciseId) REFERENCES exercises(id) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS index_template_exercises_templateId ON template_exercises (templateId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_template_exercises_exerciseId ON template_exercises (exerciseId)")

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS meals (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                mealType TEXT NOT NULL,
                createdAt INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS meal_food_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                mealId INTEGER NOT NULL,
                foodId INTEGER NOT NULL,
                grams REAL NOT NULL,
                FOREIGN KEY(mealId) REFERENCES meals(id) ON UPDATE NO ACTION ON DELETE CASCADE,
                FOREIGN KEY(foodId) REFERENCES foods(id) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS index_meal_food_items_mealId ON meal_food_items (mealId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_meal_food_items_foodId ON meal_food_items (foodId)")

        // meal_entries (the old date-based diary) has no equivalent in the new named-meal model.
        // This is the one unavoidable data-loss point in this migration, confined to old diary entries.
        db.execSQL("DROP TABLE IF EXISTS meal_entries")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS body_measurements (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                createdAt INTEGER NOT NULL,
                weightKg REAL NOT NULL,
                heightCm REAL NOT NULL,
                chestCm REAL,
                waistCm REAL,
                abdomenCm REAL,
                hipCm REAL,
                armLeftRelaxedCm REAL,
                armLeftFlexedCm REAL,
                armRightRelaxedCm REAL,
                armRightFlexedCm REAL,
                forearmLeftCm REAL,
                forearmRightCm REAL,
                thighLeftCm REAL,
                thighRightCm REAL,
                calfLeftCm REAL,
                calfRightCm REAL,
                bodyFatPercent REAL,
                leanMassKg REAL,
                fatMassKg REAL,
                bodyWaterPercent REAL,
                tricepsFoldMm REAL,
                subscapularFoldMm REAL,
                suprailiacFoldMm REAL,
                abdominalFoldMm REAL,
                thighFoldMm REAL,
                chestFoldMm REAL,
                midaxillaryFoldMm REAL
            )
            """.trimIndent()
        )

        // Carry the old single-row profile's weight/height/chest/hip forward as the first
        // Evolução Física entry instead of silently discarding it. armCm/thighCm/calfCm have no
        // unambiguous left/right equivalent in the new schema and are the one accepted loss here.
        db.execSQL(
            """
            INSERT INTO body_measurements (createdAt, weightKg, heightCm, chestCm, hipCm)
            SELECT ${System.currentTimeMillis()}, weightKg, heightCm, chestCm, hipCm
            FROM user_profile WHERE id = 1
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE user_profile_new (
                id INTEGER NOT NULL,
                name TEXT NOT NULL,
                ageYears INTEGER NOT NULL,
                sex TEXT NOT NULL,
                activityLevel TEXT NOT NULL,
                goal TEXT NOT NULL,
                PRIMARY KEY(id)
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO user_profile_new (id, name, ageYears, sex, activityLevel, goal)
            SELECT id, '', ageYears, sex, activityLevel, goal FROM user_profile
            """.trimIndent()
        )
        db.execSQL("DROP TABLE user_profile")
        db.execSQL("ALTER TABLE user_profile_new RENAME TO user_profile")
    }
}

// Both new columns are nullable and carry no foreign key, so a plain ADD COLUMN is safe here —
// no table rebuild needed (unlike MIGRATION_1_2's workout_sessions/user_profile changes above).
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE template_exercises ADD COLUMN referenceWeightKg REAL")
        db.execSQL("ALTER TABLE workout_templates ADD COLUMN notes TEXT")
    }
}
