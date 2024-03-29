package hyperlinksSupport

import java.io.File

/**
 * Responsible for applying changes to files of a specific knowledge base
 */
class KnowledgeBaseManager {
    private val knowledgeBaseId: String
    private val workingDir: String
    private val listOfFiles = HashMap<String, File>()

    constructor(knowledgeBaseId: String, initialFile: File) {
        this.knowledgeBaseId = knowledgeBaseId
        this.workingDir = initialFile.parent
        listOfFiles[initialFile.name] = initialFile
    }

    fun moveToNextFile(fileName: String): File {
        if (!listOfFiles.containsKey(fileName)) {
            val file = File(workingDir, fileName)
            if (!file.exists()) {
                val created = file.createNewFile()
                require(created) { "Error while trying to create a file '$fileName' in '$knowledgeBaseId'" }
            }
            listOfFiles[fileName] = file
        }

        return listOfFiles[fileName]!!
    }
}