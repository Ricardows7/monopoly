# Paths
JAVA_HOME = /path/to/your/java
JAVAFX_LIB = /path/to/javafx-sdk/lib
SRC = $(wildcard *.java)
CLASS_FILES = $(SRC:.java=.class)
MAIN_CLASS = run

# Compiler and runtime settings
JAVAC = $(JAVA_HOME)/bin/javac
JAVA = $(JAVA_HOME)/bin/java
JAVAC_FLAGS = --module-path $(JAVAFX_LIB) --add-modules javafx.controls,javafx.fxml

# Targets
.PHONY: all clean run

# Default target: build all class files
all: $(CLASS_FILES)

%.class: %.java
	$(JAVAC) $(JAVAC_FLAGS) $<

# Run the application
run: all
	$(JAVA) $(JAVAC_FLAGS) $(MAIN_CLASS)

# Clean the build
clean:
	rm -f *.class

