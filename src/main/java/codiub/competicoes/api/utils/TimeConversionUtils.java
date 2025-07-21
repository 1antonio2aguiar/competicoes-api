package codiub.competicoes.api.utils;
import java.text.SimpleDateFormat;
import java.util.Optional;
public final class TimeConversionUtils {
    private TimeConversionUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static Optional<Long> convertStringToLong(String timeString) {

        // --- INÍCIO DO SEU CÓDIGO ORIGINAL ---
        if (timeString == null || timeString.isEmpty()) {
            return Optional.empty();
        }

        String[] parts = timeString.split(":", -1);
        try {

            if (parts.length == 3) {
                // Cria uma nova instância de SimpleDateFormat a cada chamada
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                sdf.setLenient(false); // Boa prática: não seja leniente com formatos inválidos
                long ms = sdf.parse(timeString).getTime();
                return Optional.of(ms);
            }

            if (parts.length == 4) {
                // Cria uma nova instância de SimpleDateFormat a cada chamada
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                sdf.setLenient(false); // Boa prática
                String msPart = parts[3];
                String timeStringToParse = timeString; // Variável local para não modificar o argumento original

                if(msPart.length() == 1){
                    timeStringToParse = timeString + "00"; // Usa a variável local
                    long ms = sdf.parse(timeStringToParse).getTime();
                    return Optional.of(ms);
                } else {
                    if(msPart.length() == 2){
                        timeStringToParse = timeString + "0"; // Usa a variável local
                        long ms = sdf.parse(timeStringToParse).getTime();
                        return Optional.of(ms);
                    } else if (msPart.length() == 3) { // Verifica explicitamente 3 dígitos
                        // Nenhuma modificação necessária na string se já tem 3 dígitos
                        long ms = sdf.parse(timeStringToParse).getTime();
                        return Optional.of(ms);
                    } else {
                        // Comprimento inválido para milissegundos (0 ou >3)
                        System.err.println("Erro: Parte dos milissegundos tem comprimento inválido: " + msPart.length());
                        return Optional.empty();
                    }
                }
            }
            // Se parts.length não for 3 nem 4
            System.err.println("Erro: Número inesperado de partes: " + parts.length);
            return Optional.empty();

        } catch (Exception e) {
            System.err.println("Erro ao fazer parse da string de tempo '" + timeString + "': " + e.getMessage());
            return Optional.empty();
        }
        // --- FIM DO SEU CÓDIGO ORIGINAL ---
    }
}
