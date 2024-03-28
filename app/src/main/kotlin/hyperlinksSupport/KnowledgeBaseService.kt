package hyperlinksSupport

import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.io.path.Path
import kotlin.random.Random

class KnowledgeBaseService {
    private val knowledgeBases = HashMap<String, String>()
    private val filesDirectory = Path(System.getProperty("user.dir"), "files").toString()

    public fun generateRandomString(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the characters to choose from
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    public fun createKnowledgeBase(): File {
        // Get name for the initial file from the user
        val scanner = Scanner(System.`in`)
        print("Please, enter the name for the initial file: ")
        val fileName = scanner.nextLine()

        // Generate a knowledge base ID and create an empty directory
        val knowledgeBaseId = generateKnowledgeBaseId()
        val directory = File(filesDirectory, knowledgeBaseId)
        var created = directory.mkdirs()
        require(created) { "Error while trying to create a directory for a new knowledge base." }

        // Create the initial file in the newly created directory
        val initFile = File(directory.path, fileName)
        created = initFile.createNewFile()
        require(created) { "Error while trying to create the initial file for a new knowledge base." }

        // Save knowledge base ID in the `knowledgeBases` map.
        registerKnowledgeBase(knowledgeBaseId, fileName)

        return initFile
    }

    private fun generateKnowledgeBaseId(): String {
        var knowledgeBaseId = generateRandomString(10)
        var directory = File(filesDirectory, knowledgeBaseId)
        while (directory.exists()) {
            knowledgeBaseId = generateRandomString(10)
            directory = File(filesDirectory, knowledgeBaseId)
        }
        return knowledgeBaseId
    }

    private fun registerKnowledgeBase(knowledgeBaseId: String, initFileName: String) {
        // Check that the directory where the knowledge base is located exists
        val directory = File(filesDirectory, knowledgeBaseId)
        require(directory.exists()) { "Directory '$knowledgeBaseId' does not exist." }
        require(directory.isDirectory) { "'$knowledgeBaseId' is expected to be a directory." }

        // Check that the initial file of the knowledge base exists
        val initFile = File(directory.path, initFileName)
        require(initFile.exists()) { "Initial file with the name '$knowledgeBaseId:$initFileName' does not exist." }
        require(initFile.isFile) { "'$knowledgeBaseId:$initFileName' is expected to be a file."}

        // Save the mapping between knowledge base ID and the corresponding initial file
        knowledgeBases[knowledgeBaseId] = initFileName
    }

    public fun removeKnowledgeBase(knowledgeBaseId: String) {
        require(knowledgeBases.containsKey(knowledgeBaseId)) { "Unknown knowledge base $knowledgeBaseId." }

        // Delete files that were used by the knowledge base
        val directory = File(filesDirectory, knowledgeBaseId)
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()!!.forEach { it.delete() }
            require(directory.listFiles()!!.isEmpty()) { "Not all files were removed from '$knowledgeBaseId'" }
            directory.delete()
        }

        // Remove the mapping between knowledge base ID and the corresponding initial file
        knowledgeBases.remove(knowledgeBaseId)
    }
}