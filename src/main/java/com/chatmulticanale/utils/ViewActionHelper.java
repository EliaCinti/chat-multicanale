package com.chatmulticanale.utils;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.exception.CommandException;
import java.util.List;
import java.util.Optional;

/**
 * Classe helper che fornisce azioni riutilizzabili per la UI delle viste.
 * Contiene logica per flussi complessi, come avviare una chat privata da un messaggio.
 */
public final class ViewActionHelper {

    // Costruttore privato per evitare istanziazione
    private ViewActionHelper() {
    }

    /**
     * Gestisce il flusso completo per avviare una chat privata partendo da un messaggio selezionato.
     * <p>
     * Verifica la presenza di messaggi nella pagina corrente, chiede all'utente l'ID del messaggio
     * di origine, valida l'ID rispetto ai messaggi visibili, e invoca il controller per l'azione.
     * Comunica all'utente l'esito dell'operazione e include gestione di comandi di navigazione.
     *
     * @param messaggiVisibili lista dei {@link MessaggioDTO} attualmente mostrati all'utente
     * @param idUtenteLoggato  identificativo dell'utente che richiede l'azione
     * @param controller       istanza di {@link InterazioneUtenteController} per eseguire la creazione della chat
     * @throws CommandException se l'utente inserisce un comando di navigazione (es. "/b" per tornare indietro)
     */
    public static void avviaChatPrivataDaListaMessaggi(
            List<MessaggioDTO> messaggiVisibili,
            int idUtenteLoggato,
            InterazioneUtenteController controller) throws CommandException {
        if (messaggiVisibili.isEmpty()) {
            ViewUtils.println(ColorUtils.ANSI_YELLOW +
                    "Non ci sono messaggi in questa pagina da cui partire." +
                    ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue("Premi Invio per continuare...");
            return;
        }

        try {
            int idMessaggioOrigine = InputUtils.readInt(
                    "Inserisci l'ID del messaggio da cui avviare la chat privata: ");

            boolean idValido = messaggiVisibili.stream()
                    .anyMatch(m -> m.getIdMessaggio() == idMessaggioOrigine);

            if (idValido) {
                Optional<String> errorOptional = controller.avviaNuovaChatPrivata(
                        idMessaggioOrigine, idUtenteLoggato);

                if (errorOptional.isEmpty()) {
                    ViewUtils.println(ColorUtils.ANSI_GREEN +
                            "\nChat privata avviata con successo!" +
                            ColorUtils.ANSI_RESET);
                    ViewUtils.println(
                            "Ora puoi accedervi dalla sezione 'Chat Private'.");
                } else {
                    ViewUtils.println(ColorUtils.ANSI_RED +
                            "\nERRORE: Impossibile avviare la chat." +
                            ColorUtils.ANSI_RESET);
                    ViewUtils.println(ColorUtils.ANSI_RED +
                            "Motivo: " + errorOptional.get() +
                            ColorUtils.ANSI_RESET);
                }
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED +
                        "ID non valido. Puoi avviare una chat solo da un messaggio presente in questa pagina." +
                        ColorUtils.ANSI_RESET);
            }
            InputUtils.pressEnterToContinue("");

        } catch (NumberFormatException e) {
            ViewUtils.println(ColorUtils.ANSI_RED +
                    "Per favore, inserisci un ID numerico." +
                    ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue("");
        }
    }
}