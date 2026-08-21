// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}

// O caminho do projeto (D:\Faculdade\Git\StällFit) contém o caractere não-ASCII "ä". O JVM da
// JetBrains Runtime usada aqui decodifica o arquivo @argfile de classpath do worker de testes
// (usado pelo Gradle no Windows quando o classpath é muito longo) como Cp1252 em vez de UTF-8,
// corrompendo esse caractere e fazendo qualquer classe dentro de app/build/... não ser encontrada
// (ClassNotFoundException em testDebugUnitTest mesmo com a classe compilada corretamente).
// Redireciona a pasta de build para uma pasta irmã sem acento, eliminando o caractere problemático
// de todo caminho usado no classpath — sem precisar renomear a pasta do projeto.
subprojects {
    layout.buildDirectory.set(file("${rootDir.parentFile}/StallFit-build/${project.name}"))
}
