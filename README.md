# **HTTPS Simulator**

Este projeto é uma implementação de um simulador de comunicação segura utilizando conceitos de criptografia. Ele foi desenvolvido como parte de um trabalho prático, abordando a troca de chaves com Diffie-Hellman e a troca segura de mensagens usando o algoritmo AES em modo CBC.

## **Funcionalidades**
O simulador implementa as seguintes funcionalidades:

1. **Geração de Chaves com Diffie-Hellman**:
    - Geração de um valor privado `a`.
    - Cálculo do valor público `A = g^a mod p`.
    - Recebimento de `B` (valor público do outro participante).
    - Cálculo da chave compartilhada `V = B^a mod p`.
    - Derivação da chave de sessão `S` como os 128 bits menos significativos do hash SHA-256 de `V`.

2. **Troca Segura de Mensagens**:
    - Decifração de mensagens no formato `[128 bits IV][mensagem cifrada]`.
    - Inversão do conteúdo da mensagem decifrada.
    - Cifra do conteúdo invertido com um novo IV gerado aleatoriamente no formato `[128 bits IV][mensagem cifrada]`.

## **Arquitetura do Código**
O projeto está organizado nos seguintes pacotes:

### **1. `config`**
Contém a classe `Constants`, que armazena valores constantes utilizados no projeto, como `p`, `g`, configurações do AES e algoritmos de hash.

### **2. `parts`**
- **`Parte1`**:
  Implementa a geração de chaves Diffie-Hellman e cálculos relacionados.
- **`Parte2`**:
  Implementa o processamento de mensagens cifradas, incluindo decifração, inversão e recifração.

### **3. `security`**
Contém a classe `CryptographyUtils`, que encapsula operações criptográficas, como:
- Geração de IVs aleatórios.
- Encriptação e decriptação com AES.
- Conversão de dados para/da representação hexadecimal.

### **4. `utils`**
Contém a classe `FileUtils`, que gerencia operações de leitura e escrita de arquivos.

---

## **Requisitos**
Para executar o projeto, você precisa de:
- **Java JDK 17** ou superior (recomendado JDK 21 para utilizar strings multilinha e recursos modernos).
- Um editor ou IDE (IntelliJ IDEA, Eclipse, etc.).
- Git para controle de versão (opcional).

---

## **Como Executar**

### **1. Clonar o Repositório**
Clone este repositório para sua máquina local:
```bash
git clone https://github.com/SEU_USUARIO/HTTPS-Simulator.git
cd HTTPS-Simulator
```

### **2. Compilar o Projeto**
Certifique-se de que o JDK está configurado corretamente. Para compilar o projeto, execute:
```bash
javac -d out src/**/*.java
```

### **3. Executar o Projeto**
Para executar o projeto, utilize o seguinte comando:
```bash
java -cp out Main
```

### **4. Fluxo de Execução**
O programa solicita ao usuário que escolha executar a **Parte 1** (Geração de Chaves) ou a **Parte 2** (Processamento de Mensagens):
1. **Parte 1**:
    - Gera `a` e `A`, salva em arquivos `a.txt` e `AA.txt`.
    - Calcula `S` após receber `B` do professor, salva em `S.key`.

2. **Parte 2**:
    - Decifra a mensagem armazenada em `message.enc`.
    - Inverte o conteúdo da mensagem.
    - Cifra o conteúdo invertido com um novo IV, salvando em `message_inverted.enc`.

---

## **Estrutura do Projeto**
```plaintext
src/
├── config/
│   └── Constants.java       # Constantes do projeto (valores de p, g, etc.)
├── parts/
│   ├── Parte1.java          # Implementação da geração de chaves Diffie-Hellman
│   └── Parte2.java          # Processamento seguro de mensagens
├── security/
│   └── CryptographyUtils.java # Operações criptográficas (AES, hash, IV)
├── utils/
│   └── FileUtils.java       # Gerenciamento de arquivos
└── Main.java                # Classe principal para executar o programa
```

---

## **Detalhes de Implementação**

### **Geração de Chaves (Parte 1)**
1. **Valor Privado `a`**:
    - É gerado usando `SecureRandom` e tem comprimento inferior a `p`.

2. **Valor Público `A`**:
    - Calculado como `g^a mod p` e salvo no arquivo `AA.txt`.

3. **Chave Compartilhada `V`**:
    - Após receber `B` (do professor), calcula `V = B^a mod p`.

4. **Chave de Sessão `S`**:
    - Derivada de `SHA-256(V)`, utilizando os 128 bits menos significativos.

### **Troca de Mensagens (Parte 2)**
1. **Decifração**:
    - Utiliza AES em modo CBC com o IV extraído dos primeiros 16 bytes da mensagem.

2. **Inversão**:
    - O texto decifrado é invertido.

3. **Recifração**:
    - Gera um novo IV aleatório e cifra a mensagem invertida no formato especificado.

---

## **Testes**
Certifique-se de que o fluxo funciona conforme esperado:
1. **Parte 1**:
    - Confirme que os valores de `a` e `A` são salvos corretamente.
    - Verifique o cálculo de `V` e `S`.

2. **Parte 2**:
    - Teste com diferentes mensagens cifradas.
    - Certifique-se de que o formato `[IV][mensagem cifrada]` está sendo respeitado.

---

## **Problemas Comuns**
1. **Chave ou IV Inválidos**:
    - Certifique-se de que `S.key` foi gerado corretamente antes de executar a Parte 2.

2. **Conflitos de Arquivo**:
    - Apague ou mova arquivos existentes se desejar reiniciar o fluxo.

3. **Erros de Execução**:
    - Confirme que o JDK instalado é compatível (JDK 17 ou superior).