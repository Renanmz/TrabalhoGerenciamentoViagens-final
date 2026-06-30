package com.senac.gerenciamentoviagem.MainTelas.Roteiro

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// GeminiDTO.kt
data class GeminiRequest(val contents: List<Content>)
data class Content(val parts: List<Part>)
data class Part(val text: String)

data class GeminiResponse(val candidates: List<Candidate>)
data class Candidate(val content: Content)

// GeminiService.kt
interface GeminiService {
    // Usando exatamente o modelo que funcionou no seu teste de Postman
    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    suspend fun gerarRoteiro(
        @Header("x-goog-api-key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
