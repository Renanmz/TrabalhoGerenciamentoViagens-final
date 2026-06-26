package com.senac.gerenciamentoviagem.MainTelas.Principal.Roteiro

import com.senac.gerenciamentoviagem.BuildConfig

class RoteiroRepository(private val service: GeminiService) {
    private val API_KEY = BuildConfig.GEMINI_API_KEY

    suspend fun obterRoteiro(
        cidade: String,
        dias: Int,
        interesses: String,
        orcamento: Float,
        tipoViagem: String
    ): String {
        val prompt = """
            Crie um roteiro de viagem de $tipoViagem para $cidade de $dias dias com foco em $interesses. 
            O orçamento total disponível é de R$ $orcamento. 
            Responda em Português, não responda diretamenta a esse prompt, use tópicos e sugira atividades que caibam no orçamento informado e sejam adequadas para uma viagem de $tipoViagem.
        """.trimIndent()

        val request = GeminiRequest(listOf(Content(listOf(Part(prompt)))))
        val response = service.gerarRoteiro(API_KEY, request)

        return response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: "Não foi possível gerar o roteiro."
    }
}
