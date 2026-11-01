import spock.lang.Specification
import spock.lang.Unroll
import java.nio.charset.StandardCharsets

class BoletimSpec extends Specification {

    Boletim boletim = new Boletim()

    @Unroll
    def "Issue #1: calcularMedia deve considerar todas as notas da lista para #descricao"() {
        when:
        def media = boletim.calcularMedia(notas)
        def mediaFormatada = String.format(Locale.US, "%.2f", media)

        then:
        mediaFormatada == mediaEsperada

        where:
        descricao                      | notas             || mediaEsperada
        "Ana"                          | [8.0, 7.5, 9.0]   || "8.17"
        "Bruno"                        | [5.0, 6.0, 7.0]   || "6.00"
        "Carla"                        | [10.0, 4.0, 7.0]  || "7.00"
        "Diego"                        | [3.0, 9.0, 9.0]   || "7.00"
        "apenas primeira nota > zero"  | [9.0, 0.0, 0.0]   || "3.00"
    }

    @Unroll
    def "Issues #2 e #3: situacao deve retornar '#situacaoEsperada' para média #media"() {
        expect:
        boletim.situacao(media, 7.0, 5.0) == situacaoEsperada

        where:
        media || situacaoEsperada
        0.00  || "Reprovado"
        4.99  || "Reprovado"
        5.00  || "Recuperação"
        6.00  || "Recuperação"
        6.99  || "Recuperação"
        7.00  || "Aprovado"
        7.01  || "Aprovado"
        8.17  || "Aprovado"
    }

    def "Execução completa do boletim deve aplicar as constantes corretas e gerar a saída esperada"() {
        given:
        PrintStream outOriginal = System.out
        ByteArrayOutputStream buffer = new ByteArrayOutputStream()
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8))

        when:
        try {
            new Boletim().run()
        } finally {
            System.setOut(outOriginal)
        }

        and:
        List<String> linhas = buffer.toString(StandardCharsets.UTF_8).readLines()

        then:
        linhas == [
            "=== Boletim da turma ===",
            "Ana: média 8.17 - Aprovado",
            "Bruno: média 6.00 - Recuperação",
            "Carla: média 7.00 - Aprovado",
            "Diego: média 7.00 - Aprovado"
        ]
    }
}

