# Usage
1. **Build** the tool.
2. **Run** it with the target directory as an argument:
```
java -jar jar-indexer.jar <path-to-directory-with-jars>
```
The tool will scan all JAR files in the specified directory.

3. **Output**:
After processing, the tool generates an `classesIndex.txt` file containing **all found classes**, sorted alphabetically, and `packagesIndex.txt` file containing **all found packages**, sorted alphabetically.
