# Trabalho Prático 2 - Simulador de HTTPS

## Disciplina: Segurança de Sistemas
**Autores: Cássio Jones Dhein da Silva e Artur dos Santos Gil**

---

## 📋 Introdução

Este trabalho prático tem como objetivo simular parte do protocolo HTTPS, utilizando o protocolo Diffie-Hellman para geração de chaves e o algoritmo de criptografia AES no modo CBC para troca de mensagens seguras. 

O projeto está dividido em duas etapas principais:
1. **Geração de Chave** utilizando Diffie-Hellman.
2. **Troca de Mensagens Segura** usando AES-CBC.

---

## 📂 Estrutura do Projeto

- `notebook.ipynb` - Jupyter Notebook contendo a implementação do trabalho.
- `valor_a.txt` - Arquivo que armazena o valor gerado de `a` para a etapa de cálculo de `V`.
- `README.md` - Este arquivo de documentação.

---

## ⚙️ Pré-requisitos

Certifique-se de ter as seguintes dependências instaladas antes de executar o notebook:

- Python 3.x
- Jupyter Notebook
- Bibliotecas necessárias listadas no `requirements.txt`

### 📦 Instalação das Dependências

Use o seguinte comando para instalar as dependências:

```bash
pip install -r requirements.txt
```

Conteúdo do `requirements.txt`:
```plaintext
pycryptodome==3.18.0
```

---

## 🚀 Instruções para Execução

1. **Abra o notebook Jupyter**:
   ```bash
   jupyter notebook notebook.ipynb
   ```

2. **Etapa 1: Geração da Chave**
   - Execute as células que geram o valor `a`, calculam `A` e salvam `a` no arquivo `valor_a.txt`.
   - Envie o valor de `A` (calculado no notebook) ao professor para obter o valor `B`.

3. **Etapa 2: Troca de Mensagens Segura**
   - Após receber o valor `B`, insira-o no notebook e calcule `V` para gerar a chave de sessão `S`.
   - Decifre a mensagem recebida utilizando `S`, inverta o conteúdo, e envie-a de volta cifrada com um novo IV.

---

## 📄 Detalhes da Implementação

### Etapa 1: Geração da Chave
- Um número aleatório `a` é gerado com pelo menos 30 dígitos e salvo em `valor_a.txt`.
- O valor `A` é calculado como \( A = g^a \mod p \).
- O valor de `A` é enviado ao professor para receber o valor `B`.

### Etapa 2: Troca de Mensagens Segura
- Após receber `B`, o valor `V` é calculado como \( V = B^a \mod p \).
- Uma chave de sessão `S` é gerada a partir do hash SHA-256 de `V`, utilizando os 128 bits menos significativos.
- Uma mensagem cifrada é recebida, decifrada, invertida, e enviada de volta cifrada com um novo IV.

---

## 🗂️ Arquivos

- **notebook.ipynb**: Implementação completa do trabalho.
- **valor_a.txt**: Armazena o valor `a` para reutilização.
- **requirements.txt**: Lista de dependências para o projeto.
