package com.senac.gerenciamentoviagem.MainTelas.Roteiro

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
            Formate o roteiro como esse exemplo:
            Dia 1 : [Descrição geral]
             - Detalhes da atividade 1
             - Detalhes da atividade 2
             - Detalhes da atividade 3
            Dia 2 : [Descrição geral]
             - Detalhes da atividade 1
             - Detalhes da atividade 2
             - Detalhes da atividade 3
            ...
        """.trimIndent()

        val request = GeminiRequest(listOf(Content(listOf(Part(prompt)))))
        val response = service.gerarRoteiro(API_KEY, request)

        return response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: "Não foi possível gerar o roteiro."
    }
}
