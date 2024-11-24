/**
 * Constants.java
 * <p>
 * Este arquivo contém as constantes utilizadas no projeto, incluindo valores de `p` e `g`
 * para o algoritmo Diffie-Hellman, além de configurações para criptografia AES e hash SHA-256.
 * <p>
 * Autores:
 * - Cassio Silva (c.jones@edu.pucrs.br)
 * - Arthur Gil (a.gil@edu.pucrs.br)
 * <p>
 * Data de Criação: 20/11/2024
 * Última Modificação: 23/11/2024
 *
 */

package config;

import java.math.BigInteger;

public final class Constants {
    private Constants() {
        // Impede a instanciação.
    }

    // Constantes de p e g em formato hexadecimal
    public static final String P_HEX = """
            B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C69A6A9DCA52D23B61
            6073E28675A23D189838EF1E2EE652C013ECB4AEA906112324975C3CD49B83BF
            ACCBDD7D90C4BD7098488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0
            A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708DF1FB2BC2E4A4371
            """;
    public static final String G_HEX = """
            A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507FD6406CFF14266D31
            266FEA1E5C41564B777E690F5504F213160217B4B01B886A5E91547F9E2749F4
            D7FBD7D3B9A92EE1909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A
            D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24855E6EEB22B3B2E5
            """;

    public static final BigInteger P = new BigInteger(P_HEX.replaceAll("\\s", ""), 16);
    public static final BigInteger G = new BigInteger(G_HEX.replaceAll("\\s", ""), 16);

    public static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    public static final String AES_ALGORITHM = "AES";
    public static final String HASH_ALGORITHM = "SHA-256";

}
