# Boletim da turma

Script que calcula a média de cada aluno a partir de três notas
e informa a situação final:

- Média >= 7.0: Aprovado
- Média >= 5.0: Recuperação
- Abaixo disso: Reprovado

## Pré-requisitos

- Java 17 ou superior instalado (confira com `java -version`)

Não é preciso instalar o Gradle nem o Groovy: o projeto usa o Gradle Wrapper,
que baixa tudo automaticamente na primeira execução.

## Como executar

Na pasta do projeto, rode:

Linux / macOS:

    ./gradlew run -q

Windows:

    gradlew.bat run -q

A opção `-q` esconde as mensagens do Gradle e mostra apenas a saída do programa.
A primeira execução pode demorar um pouco, pois as dependências serão baixadas.

> Se aparecer "Permission denied" no Linux/macOS, rode `chmod +x gradlew` e tente novamente.

## Estrutura do projeto

    cacandoBugMediaEscolar/
    ├── build.gradle                     # configuração do build
    ├── settings.gradle                  # nome do projeto
    ├── gradlew / gradlew.bat            # Gradle Wrapper
    └── src/main/groovy/Boletim.groovy   # código do boletim

## Problema relatado

Os professores conferiram as notas manualmente e o resultado
esperado é:

    Ana: média 8.17 - Aprovado
    Bruno: média 6.00 - Recuperação
    Carla: média 7.00 - Aprovado
    Diego: média 7.00 - Aprovado

Mas o script está mostrando valores diferentes.

## Seu desafio

Existem **pelo menos 3 bugs** escondidos no `Boletim.groovy`.
Seu objetivo é encontrar e corrigir todos, com a ajuda de um agente de IA,
até que a saída do script fique **idêntica** ao resultado esperado acima.

Os bugs se escondem uns atrás dos outros: corrigir um deles pode fazer
aparecer um problema que antes não dava para ver. Por isso, não pare no
primeiro acerto.

### Dicas para caçar os bugs

- **Rode o script depois de cada correção** e compare a saída, linha por
  linha, com o resultado esperado. Se ainda houver diferença, ainda há bug.
- **Um código sem erros não é um código correto.** O programa pode rodar
  sem nenhuma mensagem de erro e ainda assim seguir uma regra diferente da
  que a escola definiu.
- **Dê contexto para a IA.** Ela só sabe o que você mostra a ela. Compare
  as respostas quando você envia apenas o código e quando envia também as
  regras da escola e o resultado esperado descritos neste README.
- **Preste atenção nos casos de fronteira.** Pense no que deveria acontecer
  com um aluno que tira exatamente a nota de corte.
- **Entenda antes de aceitar.** Peça para a IA explicar por que cada linha
  estava errada. Se a explicação não fizer sentido para você, pergunte de
  novo.

### Checklist

- [ ] A média de todos os alunos está igual à esperada
- [ ] A situação de todos os alunos está igual à esperada
- [ ] Consigo explicar, com minhas palavras, cada bug que corrigi

## Sobre o bug

Durante a análise do arquivo `src/main/groovy/Boletim.groovy`, foram identificados e corrigidos três bugs distintos que afetavam o cálculo das médias e a classificação final dos alunos:

### [Issue #1 — Primeira nota ignorada no cálculo da média](https://github.com/nelsonlima1989/boletim-bug/issues/1)

- **Causa:** No método `calcularMedia(List notas)`, o laço `for` iniciava no índice `1` (`for (int i = 1; i < notas.size(); i++)`). Como listas em Groovy possuem indexação iniciada em `0` (*zero-based*), a primeira nota (`notas[0]`) era ignorada na soma, mas o total continuava sendo dividido por `3` (`notas.size()`).
- **Correção:** O índice inicial do laço `for` foi alterado de `i = 1` para `i = 0` (`for (int i = 0; i < notas.size(); i++)`), garantindo que todas as notas da lista sejam somadas.
- **Commit:** `11c48ca`

### [Issue #2 — Limite de recuperação incorreto](https://github.com/nelsonlima1989/boletim-bug/issues/2)

- **Causa:** A constante `MEDIA_RECUPERACAO` estava definida como `6.5`, divergindo da regra de negócio especificada neste README, que estabelece que alunos com média maior ou igual a `5.0` e menor que `7.0` devem ficar em `Recuperação`. Isso fazia com que alunos como Bruno (média `6.00`) fossem classificados incorretamente como `Reprovado`.
- **Correção:** O valor da constante `MEDIA_RECUPERACAO` foi alterado de `6.5` para `5.0` (`final MEDIA_RECUPERACAO = 5.0`).
- **Commit:** `91348d1`

### [Issue #3 — Condição de aprovação incorreta](https://github.com/nelsonlima1989/boletim-bug/issues/3)

- **Causa:** No método `situacao(media, mediaAprovacao, mediaRecuperacao)`, a verificação de aprovação utilizava o operador relacional estrito `>` (`if (media > mediaAprovacao)`). Com isso, alunos com média exatamente igual à nota de corte `7.0` (casos de Carla e Diego) não atendiam à condição `7.0 > 7.0` e acabavam classificados incorretamente como `Recuperação`.
- **Correção:** O operador de comparação foi alterado de `>` para `>=` (`if (media >= mediaAprovacao)`), contemplando médias iguais ou superiores a `7.0`.
- **Commit:** `d724ae4`

### Resultado final do boletim após as correções

```text
=== Boletim da turma ===
Ana: média 8.17 - Aprovado
Bruno: média 6.00 - Recuperação
Carla: média 7.00 - Aprovado
Diego: média 7.00 - Aprovado
```
