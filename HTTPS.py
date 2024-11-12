#%% md
# # Trabalho Prático 2 - Simulador de HTTPS
# ## Disciplina: Segurança de Sistemas
# **Autores: Cássio Jones Dhein da Silva e Artur dos Santos Gil**
# 
# ---
# 
# ## Introdução
# 
# Este trabalho prático simula parte do protocolo HTTPS, especificamente o processo de geração de chave utilizando o protocolo Diffie-Hellman e a troca de mensagens segura via AES-CBC. Este notebook contém as duas principais etapas do trabalho, conforme descrito no enunciado:
# 
# 1. **Geração da Chave**
# 2. **Troca de Mensagens Segura**
# 
# ---
#%% md
# ## Importação de Bibliotecas
# 
# Para implementar as funcionalidades necessárias, utilizaremos as seguintes bibliotecas:
# 
# - **`secrets`**: para gerar números seguros aleatoriamente.
# - **`hashlib`**: para gerar o hash SHA-256.
# - **`Crypto.Cipher`**: para implementar o AES no modo CBC, usado para cifrar e decifrar mensagens.
#%%
import secrets
import hashlib
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
#%% md
# ## Configurações Iniciais
# 
# Definimos o número primo \( p \) e o gerador \( g \) fornecidos no enunciado. Estes valores serão usados para calcular as chaves de sessão compartilhadas no processo de geração de chave Diffie-Hellman.
#%%
p_hex = (
    "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C69A6A9DCA52D23B61"
    "6073E28675A23D189838EF1E2EE652C013ECB4AEA906112324975C3CD49B83BF"
    "ACCBDD7D90C4BD7098488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0"
    "A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708DF1FB2BC2E4A4371"
)
g_hex = (
    "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507FD6406CFF14266D31"
    "266FEA1E5C41564B777E690F5504F213160217B4B01B886A5E91547F9E2749F4"
    "D7FBD7D3B9A92EE1909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A"
    "D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24855E6EEB22B3B2E5"
)

# Convertendo p e g para inteiros
p = int(p_hex, 16)
g = int(g_hex, 16)

print("Configurações Iniciais")
print(f"Valor de 'p' (decimal): {p}")
print(f"Valor de 'g' (decimal): {g}")
#%% md
# ## Atividade 1: Geração da Chave
# 
# Nesta etapa, vamos gerar a chave de sessão usando o protocolo Diffie-Hellman. O processo envolve as seguintes etapas:
# 
# 1. Gerar um valor aleatório **'a'** com pelo menos 30 dígitos.
# 2. Calcular **A** usando a fórmula \( A = g^a \mod p \) e enviar este valor ao professor.
# 3. Receber o valor **B** do professor e calcular **V** como \( V = B^a \mod p \).
# 4. Gerar a chave de sessão calculando o hash SHA-256 de **V** e extraindo os 128 bits menos significativos.
#%% md
# ## 1. Gerar um valor aleatório **'a'** com pelo menos 30 dígitos.
# 
#%% md
# ### Gerar e salvar o valor 'a'
# 
#%%
a = secrets.randbelow(p - 1)
while len(str(a)) < 30:
    a = secrets.randbelow(p - 1)
#%% md
# ### Salvar o valor de 'a' em um arquivo
#%%
with open("valor_a.txt", "w") as file:
    file.write(str(a))
#%% md
# ### 2. Calcular **A** usando a fórmula \( A = g^a \mod p \) e enviar este valor ao professor.
# 
# 
#%%
A = pow(g, a, p)
A_hex = hex(A)[2:].upper()
print(f"Valor de 'A' calculado (hexadecimal): {A_hex}")
print("Envie este valor para o professor.")
#%% md
# ## 3. Receber o valor **B** do professor e calcular **V** como \( V = B^a \mod p \).
#%% md
# ### Carregar o valor de 'a' do arquivo
#%%
with open("valor_a.txt", "r") as file:
    a = int(file.read())
#%% md
# ### Após receber 'B' do professor, calcular 'V'
# 
#%%
B_hex = "FictitiousHexValueReceivedFromProfessor"  # Substitua pelo valor real de B fornecido pelo professor
B = int(B_hex, 16)
V = pow(B, a, p)
print(f"Valor de 'V' calculado: {V}")
#%% md
# ### Calcular o hash SHA-256 de 'V' para gerar a chave de sessão 'S'
#%%
V_bytes = V.to_bytes((V.bit_length() + 7) // 8, byteorder='big')
S = hashlib.sha256(V_bytes).digest()
S_128 = S[-16:]  # 128 bits menos significativos
S_hex = S_128.hex()
print(f"Chave de Sessão 'S' (128 bits em hexadecimal): {S_hex}")
#%% md
# ## 4. Gerar a chave de sessão calculando o hash SHA-256 de **V** e extraindo os 128 bits menos significativos.
#%%
V_bytes = V.to_bytes((V.bit_length() + 7) // 8, byteorder='big')
S = hashlib.sha256(V_bytes).digest()
S_128 = S[-16:]  # 128 bits menos significativos
S_hex = S_128.hex()

print(f"Chave de Sessão 'S' (128 bits em hexadecimal): {S_hex}")
#%% md
# ## Atividade 2: Troca de Mensagens Segura
# 
# Nesta etapa, iremos realizar a troca de mensagens segura com o professor, utilizando AES no modo CBC. O processo consiste em:
# 
# 1. Receber a mensagem cifrada com o formato: [128 bits IV][mensagem].
# 2. Decifrar a mensagem utilizando a chave de sessão.
# 3. Inverter o conteúdo da mensagem e cifrá-la novamente com um IV aleatório para envio de resposta.
#%% md
# ## 1. Receber a mensagem cifrada com o formato: [128 bits IV][mensagem].
#%%
IV_hex = "Fictitious128BitIVReceived"  # Substituir pelo valor real do IV fornecido
ciphertext_hex = "FictitiousCiphertextReceived"  # Substituir pelo valor real da mensagem cifrada fornecida

IV = bytes.fromhex(IV_hex)
ciphertext = bytes.fromhex(ciphertext_hex)

print("\nAtividade 2: Troca de Mensagens Segura")
print(f"IV recebido (hexadecimal): {IV_hex}")
print(f"Mensagem cifrada recebida (hexadecimal): {ciphertext_hex}")
#%% md
# ## 2. Decifrar a mensagem utilizando a chave de sessão.
#%%
cipher = AES.new(S_128, AES.MODE_CBC, IV)
plaintext = unpad(cipher.decrypt(ciphertext), AES.block_size)
plaintext_str = plaintext.decode('utf-8')

print(f"Mensagem decifrada: {plaintext_str}")
#%% md
# ## 3. Inverter o conteúdo da mensagem e cifrá-la novamente com um IV aleatório para envio de resposta.
#%%
reversed_plaintext = plaintext[::-1]
print(f"Mensagem invertida: {reversed_plaintext.decode('utf-8')}")
#%% md
# ## Gerar um IV aleatório para a resposta
# 
#%%
IV_new = secrets.token_bytes(16)
cipher_send = AES.new(S_128, AES.MODE_CBC, IV_new)
#%% md
# ## Aplicando padding e cifrando a mensagem invertida
#%%
ciphertext_to_send = cipher_send.encrypt(pad(reversed_plaintext, AES.block_size))
#%% md
# ## Formatar a resposta para envio: [128 bits IV aleatório][mensagem cifrada]
#%%
response_hex = IV_new.hex() + ciphertext_to_send.hex()

print(f"IV gerado para resposta (hexadecimal): {IV_new.hex()}")
print(f"Mensagem cifrada para envio (hexadecimal): {response_hex}")