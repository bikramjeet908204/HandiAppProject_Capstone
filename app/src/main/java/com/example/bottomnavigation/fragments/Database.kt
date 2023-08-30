package com.example.bottomnavigation.fragments


import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import aws.sdk.kotlin.services.dynamodb.model.DynamoDbException
import kotlin.system.exitProcess


class Database {


    suspend fun putItemInTable2(
        ddb: DynamoDbClient,
        tableNameVal: String,
        key: String,
        keyVal: String,
        username: String,
        usernameVal: String,
        pass: String,
        passVal: String,
        email: String,
        emailVal: String
    ) {
        val itemValues = mutableMapOf<String, AttributeValue>()

        // Add all content to the table.
        itemValues[key] = AttributeValue.S(keyVal)
        itemValues[username] = AttributeValue.S(usernameVal)
        itemValues[pass] = AttributeValue.S(passVal)
        itemValues[email] = AttributeValue.S(emailVal)

        val request = PutItemRequest {
            tableName=tableNameVal
            item = itemValues
        }

        try {
            ddb.putItem(request)
            println(" A new item was placed into $tableNameVal.")

        } catch (ex: DynamoDbException) {
            println(ex.message)
            ddb.close()
            exitProcess(0)
        }
    }
}