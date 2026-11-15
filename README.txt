Speedometer (Minecraft Forge 1.12.2)
- Показывает скорость игрока в км/ч в левом верхнем углу (скрывается при F3).
- K — включить/выключить, J — учитывать вертикальную скорость.

Сборка:
1. Скачайте Forge MDK 1.12.2 (14.23.5.2860) с files.minecraftforge.net.
2. Замените его build.gradle, src/ на файлы из этого проекта (или просто скопируйте src/).
3. Нужна Java 8. Выполните:  gradlew setupDecompWorkspace  затем  gradlew build
4. Готовый jar: build/libs/speedometer-1.12.2-1.0.jar -> положить в папку mods.
