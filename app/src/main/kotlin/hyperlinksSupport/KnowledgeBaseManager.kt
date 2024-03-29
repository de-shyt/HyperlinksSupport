package hyperlinksSupport

import java.io.File

class KnowledgeBaseManager {
    private val knowledgeBaseId: String
    private val workingDir: String
    private var currentFile: File
    private val listOfFiles = HashMap<String, File>()

    constructor(knowledgeBaseId: String, initialFile: File) {
        this.knowledgeBaseId = knowledgeBaseId
        this.workingDir = initialFile.parent

        this.currentFile = initialFile

        listOfFiles[initialFile.name] = initialFile
    }
}