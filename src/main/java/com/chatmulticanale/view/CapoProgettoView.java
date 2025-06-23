package com.chatmulticanale.view;

import com.chatmulticanale.controller.GestioneProgettiController;
import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.ChatSupervisioneDTO;
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

/**
 * Schermata principale riservata ai Capi Progetto.
 * Offre un menu per gestire i propri progetti, canali, membri e supervisionare chat private.
 * <p>
 * Utilizza {@link GestioneProgettiController} per le operazioni sui progetti e canali
 * e {@link InterazioneUtenteController} per le interazioni utente-chat.
 */
public class CapoProgettoView implements View {

    private final GestioneProgettiController gestioneController;
    private final InterazioneUtenteController interazioneController;
    private final ComunicazioneViewHelper comunicazioneHelper;

    /**
     * Costruisce la vista passando i controller necessari.
     *
     * @param gestioneController controller per gestione progetti e canali
     * @param interazioneController controller per invio e ricezione messaggi
     */
    public CapoProgettoView(GestioneProgettiController gestioneController, InterazioneUtenteController interazioneController) {
        this.gestioneController = gestioneController;
        this.interazioneController = interazioneController;
        this.comunicazioneHelper = new ComunicazioneViewHelper(interazioneController);
    }

    /**
     * Mostra il menu principale del Capo Progetto.
     * <p>
     * Gestisce le scelte dell'utente richiamando i metodi handler corrispondenti.
     * Supporta comandi di navigazione tramite {@link CommandException}.
     *
     * @return oggetto {@link Navigazione} che indica l'azione successiva (logout/back)
     */
    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.HOME_CAPO_PROGETTO + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.WELCOME + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();

            // Menu gestione progetti
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.HOME_CAPO_PROGETTO_OPZIONI_GESTIONE_PROGETTI + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_1);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_2);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_3);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_4);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_5);
            ViewUtils.printSeparator();

            // Menu interazioni utente
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.HOME_CAPO_PROGETTO_OPZIONI_INTERAZIONE_UTENTE + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_6);
            ViewUtils.printSeparator();
            ViewUtils.println(CostantiView.HOME_CAPO_PROGETTO_OPZIONE_0);
            ViewUtils.printSeparator();

            try {
                int scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 0, 6);
                switch (scelta) {
                    case 1 -> handleVisualizzaMieiProgetti();
                    case 2 -> handleCreaCanale();
                    case 3 -> handleAggiungiUtenteACanale();
                    case 4 -> handleRimuoviUtenteDaCanale();
                    case 5 -> handleSupervisionaChatPrivate();
                    case 6 -> handleAccediACanaliEChat();
                    case 0 -> {
                        SessionManager.getInstance().logout();
                        return Navigazione.logout();
                    }
                }
            } catch (CommandException e) {
                if (e.getNavigazione().azione == Navigazione.Azione.LOGOUT) {
                    return e.getNavigazione();
                }
                ViewUtils.println(ColorUtils.ANSI_RED + "Scelta non valida." + ColorUtils.ANSI_RESET);
                InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_RIPROVARE);
            }
        }
    }

    /**
     * Mostra i progetti di cui l'utente è responsabile.
     * Permette di visualizzare l'elenco o un messaggio se non ci sono progetti.
     */
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

    /**
     * Consente di creare un nuovo canale per uno dei propri progetti.
     * Chiede selezione del progetto, nome e descrizione del canale.
     * Gestisce comandi di annullamento e comunica l'esito.
     */
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

    /**
     * Permette di aggiungere un dipendente a un canale esistente.
     * Gestisce la selezione di progetto, canale e utente, con annullamento.
     */
    private void handleAggiungiUtenteACanale() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        try {
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println(CostantiView.NO_PROGETTI);
                InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
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

    /**
     * Permette di rimuovere un dipendente da un canale.
     * Simile al flusso di aggiunta ma per rimozione.
     */
    private void handleRimuoviUtenteDaCanale() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        try {
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println(CostantiView.NO_PROGETTI);
                InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
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
     * Supervisiona le chat private originate da un progetto.
     * Mostra l'elenco e apre la chat in sola lettura.
     */
    private void handleSupervisionaChatPrivate() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

        try {
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println(CostantiView.NO_PROGETTI);
                InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_INDIETRO);
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

    /**
     * Accede alle aree di comunicazione: canali e chat private.
     * Utilizza {@link ComunicazioneViewHelper} per gestire il flusso.
     */
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
                int idUtente = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

                switch (scelta) {
                    case 1:
                        comunicazioneHelper.handleAccessoCanaliProgetto(idUtente);
                        break;
                    case 2:
                        comunicazioneHelper.handleAccessoChatPrivate(idUtente);
                        break;
                }
            } catch (CommandException e) {
                return;
            }
        }
    }

    /**
     * Mostra un prompt e lista di progetti, e restituisce l'ID selezionato.
     *
     * @param progetti lista di {@link Progetto}
     * @param prompt messaggio da mostrare
     * @return ID del progetto scelto
     * @throws CommandException se si invia un comando di navigazione
     */
    private int selezionaProgetto(List<Progetto> progetti, String prompt) throws CommandException {
        ViewUtils.println(prompt);
        progetti.forEach(p -> ViewUtils.println(String.format(CostantiView.FORMATO_ID_NOME, p.getIdProgetto(), p.getNomeProgetto())));
        ViewUtils.printSeparator();
        return InputHelper.chiediIdValido("ID Progetto: ", progetti);
    }

    /**
     * Mostra un prompt e lista di canali, e restituisce l'ID selezionato.
     *
     * @param canali lista di {@link CanaleProgetto}
     * @param prompt messaggio da mostrare
     * @return ID del canale scelto
     * @throws CommandException se si invia un comando di navigazione
     */
    private int selezionaCanale(List<CanaleProgetto> canali, String prompt) throws CommandException {
        ViewUtils.println("\n" + prompt);
        canali.forEach(c -> ViewUtils.println(String.format(CostantiView.FORMATO_ID_NOME, c.getIdCanale(), c.getNomeCanale())));
        ViewUtils.printSeparator();
        return InputHelper.chiediIdValido("ID Canale: ", canali);
    }
}
