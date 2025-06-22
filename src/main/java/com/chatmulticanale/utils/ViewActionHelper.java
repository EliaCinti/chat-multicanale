package com.chatmulticanale.utils;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.exception.CommandException;

import java.util.List;
import java.util.Optional;

/**
 * Classe helper che contiene logica riutilizzabile per azioni complesse
 * delle viste, come avviare una chat, ecc.
 */
public class ViewActionHelper {

    private ViewActionHelper() {
        // costruttore privato
    }

    /**
     * Gestisce il flusso completo per avviare una chat privata da una lista di messaggi.
     * Chiede all'utente un ID, lo valida e invoca il controller per creare la chat.
     *
     * @param messaggiVisibili La lista dei messaggi attualmente visibili, usata per la validazione.
     * @param idUtenteLoggato L'ID dell'utente che sta compiendo l'azione.
     * @param controller Il controller per eseguire l'azione di creazione della chat.
     * @throws CommandException se l'utente digita un comando di annullamento (es. /b).
     */
    public static void avviaChatPrivataDaListaMessaggi(List<MessaggioDTO> messaggiVisibili, int idUtenteLoggato, InterazioneUtenteController controller) throws CommandException {
        if (messaggiVisibili.isEmpty()) {
            ViewUtils.println(ColorUtils.ANSI_YELLOW + "Non ci sono messaggi in questa pagina da cui partire." + ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue("Premi Invio per continuare...");
            return;
        }

        try {
            int idMessaggioOrigine = InputUtils.readInt("Inserisci l'ID del messaggio da cui avviare la chat privata: ");

            boolean idValido = messaggiVisibili.stream().anyMatch(m -> m.getIdMessaggio() == idMessaggioOrigine);

            if (idValido) {
                Optional<String> errorOptional = controller.avviaNuovaChatPrivata(idMessaggioOrigine, idUtenteLoggato);

                if (errorOptional.isEmpty()) { // isEmpty() è il nuovo modo per controllare il successo
                    ViewUtils.println(ColorUtils.ANSI_GREEN + "\nChat privata avviata con successo!" + ColorUtils.ANSI_RESET);
                    ViewUtils.println("Ora puoi accedervi dalla sezione 'Chat Private'.");
                } else { // Errore
                    ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile avviare la chat." + ColorUtils.ANSI_RESET);
                    ViewUtils.println(ColorUtils.ANSI_RED + "Motivo: " + errorOptional.get() + ColorUtils.ANSI_RESET);
                }
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "ID non valido. Puoi avviare una chat solo da un messaggio presente in questa pagina." + ColorUtils.ANSI_RESET);
            }
            InputUtils.pressEnterToContinue("");

        } catch (NumberFormatException e) {
            ViewUtils.println(ColorUtils.ANSI_RED + "Per favore, inserisci un ID numerico." + ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue("");
        }
    }
}