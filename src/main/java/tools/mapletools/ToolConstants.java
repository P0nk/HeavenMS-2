package tools.mapletools;

import java.nio.file.Path;

class ToolConstants {
    static final Path INPUT_DIRECTORY = Path.of("tools/input");
    static final Path OUTPUT_DIRECTORY = Path.of("tools/output");
    static final String SCRIPTS_PATH = "scripts";
    static final String HANDBOOK_PATH = "handbook";

    static Path getInputFile(String fileName) {
        return INPUT_DIRECTORY.resolve(fileName);
    }

    static Path getOutputFile(String fileName) {
        return OUTPUT_DIRECTORY.resolve(fileName);
    }
}
