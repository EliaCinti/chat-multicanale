package com.chatmulticanale.view;

import com.chatmulticanale.controller.GestioneProgettiController;
import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.ChatPrivataDTO;
import com.chatmulticanale.dto.ChatSupervisioneDTO;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.model.TipoContestoChat;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.*;
import com.chatmulticanale.view.costanti_view.CostantiView;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;
import java.util.List;

public class CapoProgettoView implements View {

    private final GestioneProgettiController gestioneController;
    private final InterazioneUtenteController interazioneController;

    public CapoProgettoView(GestioneProgettiController gestioneController, InterazioneUtenteController interazioneController) {
        this.gestioneController = gestioneController;
        this.interazioneController = interazioneController;
    }

    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.HOME_CAPO_PROGETTO + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.WELCOME + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();

            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.HOME_CAPO_PROGETTO_OPZIONI_GESTIONE_PROGETTI + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_1);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_2);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_3);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_4);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_5);
            ViewUtils.printSeparator();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.HOME_CAPO_PROGETTO_OPZIONI_INTERAZIONE_UTENTE + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_6);
            ViewUtils.printSeparator();
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_0);
            ViewUtils.printSeparator();

            int scelta;
            try {
                scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 0, 6);
            } catch (CommandException e) {
                if (e.getNavigazione().azione == Navigazione.Azione.LOGOUT) {
                    return e.getNavigazione();
                }
                ViewUtils.println(ColorUtils.ANSI_RED + "Errore: Inserisci un numero valido." + ColorUtils.ANSI_RESET);
                continue;
            }

            switch (scelta) {
                case 1:
                    handleVisualizzaMieiProgetti();
                    break;
                case 2:
                    handleCreaCanale();
                    break;
                case 3:
                    handleAggiungiUtenteACanale();
                    break;
                case 4:
                    handleRimuoviUtenteDaCanale();
                    break;
                case 5:
                    handleSupervisionaChatPrivate();
                    break;
                case 6:
                    handleAccediACanaliEChat();
                    break;
                case 0:
                    SessionManager.getInstance().logout();
                    return Navigazione.logout();
                default:
            }
        }
    }

    private void handleVisualizzaMieiProgetti() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.I_MIEI_PROGETTI + ColorUtils.ANSI_RESET);
        ViewUtils.printSeparator();
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
        if (mieiProgetti.isEmpty()) {
            ViewUtils.println(CostantiView.VISUALIZZA_PROGETTI_RESPONSABILE_DI_NESSUN_PROGETTO);
        } else {
            ViewUtils.println(CostantiView.VISUALIZZA_PROGETTI_ELENCO_PROGETTI);
            mieiProgetti.forEach(p -> {
                String riga = String.format(CostantiView.FORMATO_ID_NOME, p.getIdProgetto(), p.getNomeProgetto());
                ViewUtils.println(riga);
            });
        }
        ViewUtils.printSeparator();
        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_HOME);
    }

    private void handleCreaCanale() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        try {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.CREA_NUOVO_CANALE + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.B_O_BACK);
            ViewUtils.printSeparator();
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println(CostantiView.CREA_CANALE_NO_PROGETTI);
                InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_HOME);
                return;
            }
            int idProgettoSelezionato = selezionaProgetto(mieiProgetti, CostantiView.CREA_CANALE_SELEZIONA_PROGETTO);
            ViewUtils.println(CostantiView.CREA_CANALE_DETTAGLI);
            String nomeCanale = InputUtils.askForInput(CostantiView.CREA_CANALE_NOME_CANALE);
            String descrizioneCanale = InputUtils.askForInput(
                    CostantiView.CREA_CANALE_DESCRIZIONE_CANALE,
                    s -> true, // Validatore che accetta tutto
                    ""
            );
            if (gestioneController.creaNuovoCanalePerProgetto(nomeCanale, descrizioneCanale, idProgettoSelezionato, idUtenteLoggato)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.CREA_CANALE_SUCCESSO_PARTE_1 + nomeCanale + CostantiView.CREA_CANALE_SUCCESSO_PARTE_2 + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.CREA_CANALE_ERRORE + ColorUtils.ANSI_RESET);
            }
        } catch (CommandException e) {
            ViewUtils.println(CostantiView.OPERAZIONE_ANNULLATA);
        }
        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_HOME);
    }

    private void handleAggiungiUtenteACanale() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        try {
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println(CostantiView.NO_PROGETTI);
                InputUtils.pressEnterToContinue("");
                return;
            }
            progettoLoop:
            while (true) {
                ViewUtils.clearScreen();
                ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.AGGIUNGI_DIPENDENTE_A_CANALE + ColorUtils.ANSI_RESET);
                ViewUtils.println(CostantiView.B_O_BACK_2);
                ViewUtils.printSeparator();
                int idProgetto = selezionaProgetto(mieiProgetti, CostantiView.SELEZIONA_PROGETTO);
                List<CanaleProgetto> canali = gestioneController.getCanaliDelProgetto(idProgetto);
                if (canali.isEmpty()) {
                    ViewUtils.println(ColorUtils.ANSI_YELLOW + CostantiView.NO_CANALI + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_SELEZIONE_PROGETTO);
                    continue;
                }
                while (true) {
                    try {
                        int idCanale = selezionaCanale(canali, CostantiView.AGGIUNGI_DIPENDENTE_SELEZIONE_CANALE);
                        List<Utente> dipendenti = gestioneController.getDipendentiAggiungibili(idCanale);
                        if (dipendenti.isEmpty()) {
                            ViewUtils.println(ColorUtils.ANSI_YELLOW + CostantiView.AGGIUNGI_DIPENDENTE_NO_ALTRI_DIPENDENTI + ColorUtils.ANSI_RESET);
                            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_SELEZIONE_CANALE);
                            ViewUtils.clearScreen();
                            continue;
                        }
                        ViewUtils.println(CostantiView.AGGIUNGI_DIPENDENTE_SELEZIONA_DIPENDENTE);
                        dipendenti.forEach(d -> ViewUtils.println(String.format(CostantiView.FORMATO_ID_NOME_COGNOME, d.getIdUtente(), d.getNome(), d.getCognome())));
                        ViewUtils.printSeparator();
                        int idDipendente = InputHelper.chiediIdValido(CostantiView.ID_DIPENDENTE, dipendenti);
                        if (gestioneController.aggiungiDipendenteACanale(idCanale, idDipendente)) {
                            ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.AGGIUNGI_DIPENDENTE_SUCCESSO + ColorUtils.ANSI_RESET);
                        } else {
                            ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.AGGIUNGI_DIPENDENTE_ERRORE + ColorUtils.ANSI_RESET);
                        }
                        break progettoLoop;
                    } catch (CommandException e) {
                        break;
                    }
                }
            }
        } catch (CommandException e) {
            ViewUtils.println(CostantiView.OPERAZIONE_ANNULLATA);
        }
        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_HOME);
    }

    private void handleRimuoviUtenteDaCanale() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        try {
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println(CostantiView.NO_PROGETTI);
                InputUtils.pressEnterToContinue("");
                return;
            }
            progettoLoop:
            while (true) {
                ViewUtils.clearScreen();
                ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.RIMUOVI_DIPENDENTE_DA_CANALE + ColorUtils.ANSI_RESET);
                ViewUtils.println(CostantiView.B_O_BACK_2);
                ViewUtils.printSeparator();
                int idProgetto = selezionaProgetto(mieiProgetti, CostantiView.SELEZIONA_PROGETTO);
                List<CanaleProgetto> canali = gestioneController.getCanaliDelProgetto(idProgetto);
                if (canali.isEmpty()) {
                    ViewUtils.println(ColorUtils.ANSI_YELLOW + CostantiView.NO_CANALI + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_SELEZIONE_PROGETTO);
                    continue;
                }
                while (true) {
                    try {
                        int idCanale = selezionaCanale(canali, CostantiView.RIMUOVI_DIPENDENTE_SELEZIONE_CANALE);
                        List<Utente> dipendentiNelCanale = gestioneController.getDipendentiDelCanale(idCanale);
                        if (dipendentiNelCanale.isEmpty()) {
                            ViewUtils.println(ColorUtils.ANSI_YELLOW + CostantiView.RIMUOVI_DIPENDENTE_NO_DIPENDENTI_WARNING + ColorUtils.ANSI_RESET);
                            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_SELEZIONE_CANALE);
                            ViewUtils.clearScreen();
                            continue;
                        }
                        ViewUtils.println(CostantiView.RIMUOVI_DIPENDENTE_SELEZIONA_DIPENDENTE);
                        dipendentiNelCanale.forEach(d -> ViewUtils.println(String.format(CostantiView.FORMATO_ID_NOME_COGNOME, d.getIdUtente(), d.getNome(), d.getCognome())));
                        ViewUtils.printSeparator();
                        int idDipendente = InputHelper.chiediIdValido(CostantiView.ID_DIPENDENTE, dipendentiNelCanale);
                        if (gestioneController.rimuoviDipendenteDaCanale(idCanale, idDipendente)) {
                            ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.RIMUOVI_DIPENDENTE_SUCCESSO + ColorUtils.ANSI_RESET);
                        } else {
                            ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.RIMUOVI_DIPENDENTE_ERRORE + ColorUtils.ANSI_RESET);
                        }
                        break progettoLoop;
                    } catch (CommandException e) {
                        break;
                    }
                }
            }
        } catch (CommandException e) {
            ViewUtils.println(CostantiView.OPERAZIONE_ANNULLATA);
        }
        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_HOME);
    }

    /**
     * Gestisce il flusso per la supervisione (sola lettura) delle chat private
     * originate da un progetto di cui l'utente è responsabile.
     */
    private void handleSupervisionaChatPrivate() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

        try {
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println(CostantiView.NO_PROGETTI);
                InputUtils.pressEnterToContinue("");
                return;
            }

            while (true) {
                ViewUtils.clearScreen();
                ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.SUPERVISIONE_CHAT_PRIVATE + ColorUtils.ANSI_RESET);
                ViewUtils.println(CostantiView.B_O_BACK_2);
                ViewUtils.printSeparator();


                int idProgetto = selezionaProgetto(mieiProgetti, CostantiView.SUPERVISIONE_CHAT_SELEZIONA_PROGETTO);

                List<ChatSupervisioneDTO> chatDaSupervisionare = gestioneController.getChatPrivateDaSupervisionare(idProgetto, idUtenteLoggato);

                if (chatDaSupervisionare == null) {
                    ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.SUPERVISIONE_CHAT_ERRORE + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_RIPROVARE);
                    continue; // Ripresenta la lista dei progetti
                }

                if (chatDaSupervisionare.isEmpty()) {
                    ViewUtils.println(ColorUtils.ANSI_YELLOW + CostantiView.SUPERVISIONE_CHAT_NO_CHAT_WARNING + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_SELEZIONE_PROGETTO);
                    continue; // Ripresenta la lista dei progetti
                }

                // Se siamo qui, abbiamo trovato delle chat. Mostriamole e usciamo dal loop.
                String nomeProgettoSelezionato = mieiProgetti.stream()
                        .filter(p -> p.getIdProgetto() == idProgetto).findFirst()
                        .map(Progetto::getNomeProgetto).orElse("");

                ViewUtils.println(CostantiView.SUPERVISIONE_CHAT_ELENCO_CHAT + nomeProgettoSelezionato + "':");
                ViewUtils.printSeparator();
                String header = String.format("%-5s | %-20s | %-25s | %-18s | %s", "ID", "Data Creazione", "Partecipanti", "Canale Origine", "Messaggio Origine");
                ViewUtils.println(ColorUtils.ANSI_BOLD + header + ColorUtils.ANSI_RESET);

                chatDaSupervisionare.forEach(chat -> {
                    String partecipanti = chat.getCreatoreChatUsername() + " <-> " + chat.getPartecipanteChatUsername();
                    String riga = String.format("%-5d | %-20s | %-25s | %-18s | '%s'",
                            chat.getIdChat(), chat.getDataCreazioneChat(), partecipanti,
                            chat.getCanaleOrigineNome(), chat.getMessaggioOrigineContenuto());
                    ViewUtils.println(riga);
                });
                ViewUtils.printSeparator();

                int idChatSelezionata = InputHelper.chiediIdValido(CostantiView.SUPERVISIONE_CHAT_INSERISCI_ID_CHAT, chatDaSupervisionare);

                new ChatView(interazioneController, idChatSelezionata, TipoContestoChat.CHAT_PRIVATA, true, idUtenteLoggato).show();

                // Dopo aver visualizzato la chat, l'operazione è considerata conclusa.
                // Usciamo dal loop principale di questo metodo.
                break;
            }
        } catch (CommandException e) {
            ViewUtils.println(CostantiView.OPERAZIONE_ANNULLATA);
        }

        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_HOME);
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
                int scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 1, 2);

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
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.ACCESSO_AI_CANALI_DI_PROGETTO + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK_2);
        ViewUtils.printSeparator();
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<CanaleProgetto> mieiCanali = interazioneController.getCanaliUtente(idUtenteLoggato);
        if (mieiCanali.isEmpty()) {
            ViewUtils.println(CostantiView.NO_PARTECIPAZIONE_A_CANALI);
            InputUtils.pressEnterToContinue("");
            return;
        }
        String prompt = CostantiView.SELEZIONA_CANALE_PER_MESSAGGI;
        int idCanaleSelezionato = selezionaCanale(mieiCanali, prompt);
        new ChatView(interazioneController, idCanaleSelezionato, TipoContestoChat.CANALE_PROGETTO, false, idUtenteLoggato).show();
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
                }
            } catch (CommandException e) {
                // Cattura il /b e ripropone il sotto-menu
                return;
            }
        }
    }

    /**
     * Mostra la lista delle chat private dell'utente e permette di selezionarne
     * una per visualizzarne la conversazione.
     * @throws CommandException se l'utente usa /b.
     */
    private void handleVisualizzaMieChat() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.LE_TUE_CHAT_PRIVATE + ColorUtils.ANSI_RESET);
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<ChatPrivataDTO> mieChat = interazioneController.getMieChatPrivate(idUtenteLoggato);

        if (mieChat.isEmpty()) {
            ViewUtils.println(CostantiView.VISUALIZZA_CHAT_NO_CHAT);
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
            return;
        }

        ViewUtils.println(CostantiView.VISUALIZZA_CHAT_ELENCO_CHAT);
        String header = String.format("%-5s | %-20s | %s", "ID", "Avviata il", "Conversazione con");
        ViewUtils.println(ColorUtils.ANSI_BOLD + header + ColorUtils.ANSI_RESET);

        mieChat.forEach(chat -> {
            String riga = String.format("%-5d | %-20s | %s",
                    chat.getIdChat(), chat.getDataCreazione(), chat.getAltroPartecipanteUsername());
            ViewUtils.println(riga);
        });
        ViewUtils.printSeparator();

        // Aggiungiamo il tipo ChatPrivataDTO a InputHelper
        int idChatSelezionata = InputHelper.chiediIdValido(CostantiView.VISUALIZZA_CHAT_INSERISCI_ID_CHAT, mieChat);

        new ChatView(
                interazioneController,
                idChatSelezionata,
                TipoContestoChat.CHAT_PRIVATA,
                false, // Non è in sola lettura, puoi partecipare
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

        int idCanaleSelezionato = selezionaCanale(mieiCanali, CostantiView.AVVIA_NUOVA_CHAT_SELEZIONA_CANALE);
        int idMessaggioSelezionato = selezionaMessaggioDaCanale(idCanaleSelezionato, idUtenteLoggato);

        // Ora che abbiamo l'ID del messaggio, chiamiamo helper per avviare la chat
        // Creiamo una lista fittizia solo per passare la validazione dell'ID
        // all'interno di ViewActionHelper (questo è un piccolo "trucco" per riutilizzare il codice)
        MessaggioDTO msgPlaceholder = new MessaggioDTO();
        msgPlaceholder.setIdMessaggio(idMessaggioSelezionato);

        ViewActionHelper.avviaChatPrivataDaListaMessaggi(List.of(msgPlaceholder), idUtenteLoggato, this.interazioneController);

        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
    }


    private int selezionaProgetto(List<Progetto> progetti, String prompt) throws CommandException {
        ViewUtils.println(prompt);
        progetti.forEach(p -> ViewUtils.println(String.format(CostantiView.FORMATO_ID_NOME, p.getIdProgetto(), p.getNomeProgetto())));
        ViewUtils.printSeparator();
        return InputHelper.chiediIdValido("ID Progetto: ", progetti);
    }

    private int selezionaCanale(List<CanaleProgetto> canali, String prompt) throws CommandException {
        ViewUtils.println("\n" + prompt);
        canali.forEach(c -> ViewUtils.println(String.format(CostantiView.FORMATO_ID_NOME, c.getIdCanale(), c.getNomeCanale())));
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
                InputUtils.pressEnterToContinue("Premi Invio per tornare indietro...");
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
                            ViewUtils.println(ColorUtils.ANSI_RED + "ID non valido. Seleziona un ID dalla pagina corrente." + ColorUtils.ANSI_RESET);
                            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_RIPROVARE);
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
