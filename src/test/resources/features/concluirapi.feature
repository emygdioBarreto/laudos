# language: pt
Funcionalidade: Listar Frases de Concluir

  Como um cliente da API
  Quero obter uma lista das frases de concluir
  Para visualizar todas as frases cadastradas no sistema

  Cenário: Listar todas as frases de concluir com sucesso
    Dado que existam frases de concluir cadastradas no sistema:
      | id | conclusao           |
      | 1  | frase 1 de concluir |
      | 2  | frase 2 de concluir |
      | 3  | frase 3 de concluir |
    Quando eu faco uma requisicao GET para obter as frases de concluir
    Entao a resposta deve ter o status code 200
    E a resposta deve conter os dados que foram cadastrados previamente