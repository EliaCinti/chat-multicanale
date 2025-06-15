package com.chatmulticanale.view;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.ChatPrivataDTO;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.TipoContestoChat;
import com.chatmulticanale.utils.*;
import com.chatmulticanale.utils.ViewActionHelper;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;
import java.util.List;

public class DipendenteView implements View {

    private final InterazioneUtenteController interazioneController;

    public DipendenteView(InterazioneUtenteController interazioneController) {
        this.interazioneController = interazioneController;
    }

    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- HOME DIPENDENTE ---" + ColorUtils.ANSI_RESET);
            ViewUtils.println("Benvenuto, " + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();

            ViewUtils.println("1. Accedi ai tuoi canali e chat");
            ViewUtils.println("0. Logout");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readIntInRange("Seleziona un'opzione: ", 0, 1);

            switch (scelta) {
                case 1:
                    handleAccediACanaliEChat();
                    break;
                case 0:
                    SessionManager.getInstance().logout();
                    return Navigazione.logout();
            }
        }
    }

    // Da modificare in CapoProgettoView.java e DipendenteView.java

    private void handleAccediACanaliEChat() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AREA COMUNICAZIONI ---" + ColorUtils.ANSI_RESET);
            ViewUtils.println("Digita '/b' o '/back' per tornare alla Home Principale.");
            ViewUtils.printSeparator();
            ViewUtils.println("1. Accedi ai Canali di Progetto");
            ViewUtils.println("2. Accedi alle Chat Private");
            ViewUtils.printSeparator();

            try {
                // Ora il prompt per l'input è più generico
                int scelta = InputUtils.readIntInRange("Seleziona un'opzione: ", 1, 2);

                switch (scelta) {
                    case 1:
                        handleAccessoCanaliProgetto();
                        break;
                    case 2:
                        handleAccessoChatPrivate();
                        break;
                }
            } catch (CommandException e) {
                return;
            }
        }
    }

    private void handleAccessoCanaliProgetto() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- I MIEI CANALI DI PROGETTO ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per tornare al menu precedente.");
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<CanaleProgetto> mieiCanali = interazioneController.getCanaliUtente(idUtenteLoggato);

        if (mieiCanali.isEmpty()) {
            ViewUtils.println("Non partecipi a nessun canale di progetto.");
            InputUtils.pressEnterToContinue("");
            return;
        }

        String prompt = "Seleziona un canale per visualizzarne i messaggi:";
        int idCanaleSelezionato = selezionaCanale(mieiCanali, prompt);

        new ChatView(
                interazioneController,
                idCanaleSelezionato,
                TipoContestoChat.CANALE_PROGETTO,
                false, // Non è in sola lettura
                idUtenteLoggato
        ).show();
    }

    private void handleAccessoChatPrivate() throws CommandException {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AREA CHAT PRIVATE ---" + ColorUtils.ANSI_RESET);
            ViewUtils.println("Digita '/b' o '/back' per tornare al menu precedente.");
            ViewUtils.printSeparator();
            ViewUtils.println("1. Visualizza le tue chat");
            ViewUtils.println("2. Avvia una nuova chat da un messaggio di un canale");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readIntInRange("Seleziona un'opzione: ", 1, 2);

            try {
                switch (scelta) {
                    case 1:
                        handleVisualizzaMieChat();
                        break;
                    case 2:
                        handleAvviaNuovaChatDaCanale();
                        break;
                }
            } catch (CommandException e) {
                // Cattura il /b e ripropone il sotto-menu
                return;
            }
        }
    }

    private void handleVisualizzaMieChat() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- LE TUE CHAT PRIVATE ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per tornare al menu precedente.");
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<ChatPrivataDTO> mieChat = interazioneController.getMieChatPrivate(idUtenteLoggato);

        if (mieChat.isEmpty()) {
            ViewUtils.println("Non hai nessuna chat privata attiva.");
            InputUtils.pressEnterToContinue("Premi Invio per tornare indietro...");
            return;
        }

        ViewUtils.println("Elenco delle tue conversazioni private:");
        String header = String.format("%-5s | %-20s | %s", "ID", "Avviata il", "Conversazione con");
        ViewUtils.println(ColorUtils.ANSI_BOLD + header + ColorUtils.ANSI_RESET);

        mieChat.forEach(chat -> {
            String riga = String.format("%-5d | %-20s | %s",
                    chat.getIdChat(), chat.getDataCreazione(), chat.getAltroPartecipanteUsername());
            ViewUtils.println(riga);
        });
        ViewUtils.printSeparator();

        int idChatSelezionata = InputHelper.chiediIdValido("Inserisci l'ID della chat da visualizzare: ", mieChat);

        new ChatView(
                interazioneController,
                idChatSelezionata,
                TipoContestoChat.CHAT_PRIVATA,
                false, // Non è in sola lettura
                idUtenteLoggato
        ).show();
    }

    private void handleAvviaNuovaChatDaCanale() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AVVIA NUOVA CHAT PRIVATA ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per tornare indietro in qualsiasi momento.");
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<CanaleProgetto> mieiCanali = interazioneController.getCanaliUtente(idUtenteLoggato);

        if (mieiCanali.isEmpty()) {
            ViewUtils.println("Devi partecipare ad almeno un canale per poter avviare una chat.");
            InputUtils.pressEnterToContinue("");
            return;
        }

        String promptCanale = "Seleziona il canale contenente il messaggio da cui partire:";
        int idCanaleSelezionato = selezionaCanale(mieiCanali, promptCanale);

        int idMessaggioSelezionato = selezionaMessaggioDaCanale(idCanaleSelezionato, idUtenteLoggato);

        // Ora che abbiamo l'ID del messaggio, chiamiamo helper per avviare la chat
        // Creiamo una lista fittizia solo per passare la validazione dell'ID
        // all'interno di ViewActionHelper (questo è un piccolo "trucco" per riutilizzare il codice)
        MessaggioDTO msgPlaceholder = new MessaggioDTO();
        msgPlaceholder.setIdMessaggio(idMessaggioSelezionato);

        ViewActionHelper.avviaChatPrivataDaListaMessaggi(List.of(msgPlaceholder), idUtenteLoggato, this.interazioneController);

        InputUtils.pressEnterToContinue("Premi Invio per tornare al menu precedente...");
    }

    private int selezionaCanale(List<CanaleProgetto> canali, String prompt) throws CommandException {
        ViewUtils.println("\n" + prompt);
        canali.forEach(c -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s", c.getIdCanale(), c.getNomeCanale())));
        ViewUtils.printSeparator();
        return InputHelper.chiediIdValido("ID Canale: ", canali);
    }

    /**
     * Permette all'utente di navigare tra le pagine dei messaggi di un canale
     * e di selezionarne uno tramite il suo ID.
     *
     * @param idCanale L'ID del canale da cui selezionare il messaggio.
     * @param idUtenteLoggato L'ID dell'utente che sta navigando.
     * @return L'ID del messaggio selezionato.
     * @throws CommandException se l'utente digita /b per annullare la selezione.
     */
    private int selezionaMessaggioDaCanale(int idCanale, int idUtenteLoggato) throws CommandException {
        int paginaCorrente = 1;

        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- Seleziona un Messaggio (Pagina " + paginaCorrente + ") ---" + ColorUtils.ANSI_RESET);
            ViewUtils.printSeparator();

            List<MessaggioDTO> messaggi = interazioneController.getPaginaMessaggiCanale(idCanale, idUtenteLoggato, paginaCorrente);

            if (messaggi.isEmpty() && paginaCorrente > 1) {
                ViewUtils.println("Non ci sono altre pagine.");
                paginaCorrente--;
                InputUtils.pressEnterToContinue("");
                continue;
            } else if (messaggi.isEmpty()) {
                ViewUtils.println("Nessun messaggio in questo canale.");
                InputUtils.pressEnterToContinue("Premi Invio per tornare indietro...");
                throw new CommandException(Navigazione.indietro());
            }

            messaggi.forEach(msg -> {
                String riga = String.format("ID %-4d | [%s] %s: %s",
                        msg.getIdMessaggio(), new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(msg.getTimestamp()),
                        msg.getUsernameMittente(), msg.getContenuto());
                ViewUtils.println(riga);
            });
            ViewUtils.printSeparator();

            // Chiediamo all'utente di inserire un ID o un comando di navigazione
            String input = InputUtils.askForInput("Inserisci l'ID del messaggio o naviga ([N] Pag. Succ. | [P] Pag. Prec. | [/b] Annulla): ").toLowerCase();

            switch (input) {
                case "n":
                    paginaCorrente++;
                    break;
                case "p":
                    if (paginaCorrente > 1) {
                        paginaCorrente--;
                    } else {
                        ViewUtils.println("Sei già alla prima pagina.");
                        InputUtils.pressEnterToContinue("Premi Invio per continuare...");
                    }
                    break;
                default:
                    try {
                        int idSelezionato = Integer.parseInt(input);
                        // Verifichiamo che l'ID sia valido in questa pagina
                        boolean idValido = messaggi.stream().anyMatch(m -> m.getIdMessaggio() == idSelezionato);
                        if (idValido) {
                            return idSelezionato; // Successo! Restituiamo l'ID.
                        } else {
                            ViewUtils.println(ColorUtils.ANSI_RED + "ID non valido. Seleziona un ID dalla pagina corrente." + ColorUtils.ANSI_RESET);
                            InputUtils.pressEnterToContinue("");
                        }
                    } catch (NumberFormatException e) {
                        ViewUtils.println(ColorUtils.ANSI_RED + "Comando non riconosciuto. Riprova." + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue("Premi Invio per riprovare...");
                    }
                    break;
            }
        }
    }
}
