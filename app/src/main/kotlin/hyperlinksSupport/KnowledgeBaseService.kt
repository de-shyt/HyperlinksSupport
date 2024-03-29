package hyperlinksSupport

import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.io.path.Path
import kotlin.random.Random

/**
 * Responsible for creating, setting up and removing knowledge bases.
 */
class KnowledgeBaseService {
    private val filesDirectory = Path(System.getProperty("user.dir"), "files").toString()

    /**
     * Creates a new knowledge base by creating a directory and one initial file. The name of the file is received from the user.
     * @return an instance of the `KnowledgeBaseManager` class.
     * @see KnowledgeBaseManager
     */
    fun createKnowledgeBase(): KnowledgeBaseManager {
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

        return KnowledgeBaseManager(knowledgeBaseId, initFile)
    }

    /**
     * Generates an ID for a knowledge base. ID is a string consisting of 10 random characters.
     * @return a new and unique ID for a knowledge base
     */
    private fun generateKnowledgeBaseId(): String {
        var knowledgeBaseId = generateRandomString()
        var directory = File(filesDirectory, knowledgeBaseId)
        while (directory.exists()) {
            knowledgeBaseId = generateRandomString()
            directory = File(filesDirectory, knowledgeBaseId)
        }
        return knowledgeBaseId
    }

    /**
     * Generates a string consisting of 10 random characters. The result is used as an ID for a knowledge base.
     * @return A string consisting of 10 random characters
     */
    private fun generateRandomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the characters to choose from
        return (1..10)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    /**
     * Removes files created for a specific knowledge base.
     * @param knowledgeBaseId ID of a knowledge base which files should be removed
     */
    fun removeKnowledgeBase(knowledgeBaseId: String) {
        // Delete files that were used by the knowledge base
        val directory = File(filesDirectory, knowledgeBaseId)
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()!!.forEach { it.delete() }
            require(directory.listFiles()!!.isEmpty()) { "Not all files were removed from '$knowledgeBaseId'" }
            directory.delete()
        }
    }
}