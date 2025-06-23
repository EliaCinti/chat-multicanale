package com.chatmulticanale.view;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.ChatPrivataDTO;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.TipoContestoChat;
import com.chatmulticanale.utils.*;
import com.chatmulticanale.utils.ViewActionHelper;
import com.chatmulticanale.view.costanti_view.CostantiView;
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
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.HOME_DIPENDENTE + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.WELCOME + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();

            ViewUtils.println(CostantiView.HOME_DIPENDENTE_OPZIONE_1);
            ViewUtils.println(CostantiView.HOME_DIPENDENTE_OPZIONE_0);
            ViewUtils.printSeparator();

            int scelta;
            try {
                scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 0, 1);
            } catch (CommandException e) {
                if (e.getNavigazione().azione == Navigazione.Azione.LOGOUT) {
                    return e.getNavigazione();
                }
                ViewUtils.println(ColorUtils.ANSI_RED + "Errore: Inserisci un numero valido." + ColorUtils.ANSI_RESET);
                continue;
            }

            switch (scelta) {
                case 1:
                    handleAccediACanaliEChat();
                    break;
                case 0:
                    SessionManager.getInstance().logout();
                    return Navigazione.logout();
                default:
            }
        }
    }

    private void handleAccediACanaliEChat() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.AREA_COMUNICAZIONI + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.B_O_BACK_2);
            ViewUtils.printSeparator();
            ViewUtils.println(CostantiView.ACCEDI_A_CANALI_E_CHAT_OPZIONE_1);
            ViewUtils.println(CostantiView.ACCEDI_A_CANALI_E_CHAT_OPZIONE_2);
            ViewUtils.printSeparator();

            try {
                // Ora il prompt per l'input è più generico
                int scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 1, 2);

                switch (scelta) {
                    case 1:
                        handleAccessoCanaliProgetto();
                        break;
                    case 2:
                        handleAccessoChatPrivate();
                        break;
                    default:
                }
            } catch (CommandException e) {
                return;
            }
        }
    }

    private void handleAccessoCanaliProgetto() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.ACCESSO_AI_CANALI_DI_PROGETTO + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK_2);
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<CanaleProgetto> mieiCanali = interazioneController.getCanaliUtente(idUtenteLoggato);

        if (mieiCanali.isEmpty()) {
            ViewUtils.println(CostantiView.NO_PARTECIPAZIONE_A_CANALI);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
            return;
        }

        String prompt = CostantiView.SELEZIONA_CANALE_PER_MESSAGGI;
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
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.AREA_CHAT_PRIVATE + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.B_O_BACK_2);
            ViewUtils.printSeparator();
            ViewUtils.println(CostantiView.ACCEDI_CHAT_PRIVATE_OPZIONE_1);
            ViewUtils.println(CostantiView.ACCEDI_CHAT_PRIVATE_OPZIONE_2);
            ViewUtils.printSeparator();

            int scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 1, 2);

            try {
                switch (scelta) {
                    case 1:
                        handleVisualizzaMieChat();
                        break;
                    case 2:
                        handleAvviaNuovaChatDaCanale();
                        break;
                    default:
                }
            } catch (CommandException e) {
                // Cattura il /b e ripropone il sotto-menu
                return;
            }
        }
    }

    private void handleVisualizzaMieChat() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.LE_TUE_CHAT_PRIVATE + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK_2);
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<ChatPrivataDTO> mieChat = interazioneController.getMieChatPrivate(idUtenteLoggato);

        if (mieChat.isEmpty()) {
            ViewUtils.println(CostantiView.NO_CHAT_ATTIVE);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
            return;
        }

        ViewUtils.println(CostantiView.ELENCO_CHAT_PRIVATE);
        String header = String.format("%-5s | %-20s | %s", "ID", "Avviata il", "Conversazione con");
        ViewUtils.println(ColorUtils.ANSI_BOLD + header + ColorUtils.ANSI_RESET);

        mieChat.forEach(chat -> {
            String riga = String.format("%-5d | %-20s | %s",
                    chat.getIdChat(), chat.getDataCreazione(), chat.getAltroPartecipanteUsername());
            ViewUtils.println(riga);
        });
        ViewUtils.printSeparator();

        int idChatSelezionata = InputHelper.chiediIdValido(CostantiView.VISUALIZZA_CHAT_INSERISCI_ID_CHAT, mieChat);

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
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.AVVIA_NUOVA_CHAT_PRIVATA + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK_2);
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<CanaleProgetto> mieiCanali = interazioneController.getCanaliUtente(idUtenteLoggato);

        if (mieiCanali.isEmpty()) {
            ViewUtils.println(CostantiView.AVVIA_NUOVA_CHAT_NO_CANALI);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
            return;
        }

        String promptCanale = CostantiView.AVVIA_NUOVA_CHAT_SELEZIONA_CANALE;
        int idCanaleSelezionato = selezionaCanale(mieiCanali, promptCanale);

        int idMessaggioSelezionato = selezionaMessaggioDaCanale(idCanaleSelezionato, idUtenteLoggato);

        // Ora che abbiamo l'ID del messaggio, chiamiamo helper per avviare la chat
        // Creiamo una lista fittizia solo per passare la validazione dell'ID
        // all'interno di ViewActionHelper (questo è un piccolo "trucco" per riutilizzare il codice)
        MessaggioDTO msgPlaceholder = new MessaggioDTO();
        msgPlaceholder.setIdMessaggio(idMessaggioSelezionato);

        ViewActionHelper.avviaChatPrivataDaListaMessaggi(List.of(msgPlaceholder), idUtenteLoggato, this.interazioneController);

        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
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
                ViewUtils.println(CostantiView.NO_ALTRE_PAGINE);
                paginaCorrente--;
                InputUtils.pressEnterToContinue("");
                continue;
            } else if (messaggi.isEmpty()) {
                ViewUtils.println(CostantiView.NO_MESSAGGIO_IN_CANALE);
                InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
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
                        ViewUtils.println(CostantiView.PRIMA_PAGINA);
                        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_CONTINUARE);
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
                            ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.ID_NON_VALIDO_IN_PAGINA_CORRENTE + ColorUtils.ANSI_RESET);
                            InputUtils.pressEnterToContinue("");
                        }
                    } catch (NumberFormatException e) {
                        ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.COMANDO_NON_RICONOSCIUTO + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_RIPROVARE);
                    }
                    break;
            }
        }
    }
}
