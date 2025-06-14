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
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- HOME CAPO PROGETTO ---" + ColorUtils.ANSI_RESET);
            ViewUtils.println("Benvenuto, " + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();

            ViewUtils.println(ColorUtils.ANSI_BOLD + "Opzioni di Gestione Progetti:" + ColorUtils.ANSI_RESET);
            ViewUtils.println("1. Visualizza i progetti di cui sei responsabile");
            ViewUtils.println("2. Crea un nuovo canale per un progetto (CP1)");
            ViewUtils.println("3. Aggiungi un dipendente a un canale (CP2)");
            ViewUtils.println("4. Rimuovi un dipendente da un canale (CP3)");
            ViewUtils.println("5. Supervisiona le chat private di un progetto (CP4)");
            ViewUtils.printSeparator();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "Opzioni di Interazione Utente:" + ColorUtils.ANSI_RESET);
            ViewUtils.println("6. Accedi ai tuoi canali e chat");
            ViewUtils.printSeparator();
            ViewUtils.println("0. Logout");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readInt("Seleziona un'opzione: ");

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
                    ViewUtils.println(ColorUtils.ANSI_RED + "\nScelta non valida." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("Premi Invio per riprovare...");
            }
        }
    }

    /**
     * Gestisce la visualizzazione della lista dei progetti di cui il Capo Progetto loggato è responsabile.
     * Recupera i dati tramite il controller e li formatta per la visualizzazione a console.
     * L'interazione è di sola lettura e termina con una pausa prima di tornare al menu principale.
     */
    private void handleVisualizzaMieiProgetti() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- I MIEI PROGETTI ---" + ColorUtils.ANSI_RESET);
        ViewUtils.printSeparator();

        // 1. Recupera l'ID dell'utente loggato dalla sessione
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

        // 2. Chiama il controller per ottenere la lista dei progetti
        List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);

        // 3. Mostra i risultati all'utente
        if (mieiProgetti.isEmpty()) {
            ViewUtils.println("Al momento non sei responsabile di nessun progetto.");
        } else {
            ViewUtils.println("Elenco dei progetti di cui sei responsabile:");
            mieiProgetti.forEach(p -> {
                String riga = String.format("  ID: %-5d | Nome: %s", p.getIdProgetto(), p.getNomeProgetto());
                ViewUtils.println(riga);
            });
        }

        ViewUtils.printSeparator();
        // 4. Mette in pausa per dare il tempo di leggere e poi torna al menu principale
        InputUtils.pressEnterToContinue("Premi Invio per tornare alla home...");
    }

    /**
     * Gestisce il flusso guidato per la creazione di un nuovo canale associato a un progetto.
     * L'utente seleziona prima un progetto di sua competenza e poi fornisce i dettagli del nuovo canale.
     */
    private void handleCreaCanale() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- CREA NUOVO CANALE (CP1) ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per annullare in qualsiasi momento.");
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

        // 1. Chiedi all'utente per quale progetto vuole creare il canale
        List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
        if (mieiProgetti.isEmpty()) {
            ViewUtils.println("Non hai progetti di cui sei responsabile, quindi non puoi creare canali.");
            InputUtils.pressEnterToContinue("Premi Invio per tornare alla home...");
            return;
        }

        try {
            ViewUtils.println("Seleziona il progetto per cui vuoi creare un nuovo canale:");
            mieiProgetti.forEach(p -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s", p.getIdProgetto(), p.getNomeProgetto())));
            ViewUtils.printSeparator();

            int idProgettoSelezionato = InputHelper.chiediIdValido("Inserisci l'ID del progetto: ", mieiProgetti);

            // 2. Chiedi i dettagli del nuovo canale
            ViewUtils.println("\nOra inserisci i dettagli per il nuovo canale.");
            String nomeCanale = InputUtils.askForInput("Nome del canale: ");
            String descrizioneCanale = InputUtils.askForInput("Descrizione del canale: ");

            // 3. Chiama il controller per eseguire l'azione
            if (gestioneController.creaNuovoCanalePerProgetto(nomeCanale, descrizioneCanale, idProgettoSelezionato, idUtenteLoggato)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nCanale '" + nomeCanale + "' creato con successo!" + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile creare il canale. Il nome potrebbe essere già in uso per questo progetto." + ColorUtils.ANSI_RESET);
            }

        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata.");
        }

        InputUtils.pressEnterToContinue("Premi Invio per tornare alla home...");
    }

    private void handleAggiungiUtenteACanale() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

        try {
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println("Non hai progetti da gestire.");
                InputUtils.pressEnterToContinue("");
                return;
            }

            progettoLoop:
            while (true) {
                ViewUtils.clearScreen();
                ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AGGIUNGI DIPENDENTE A CANALE (CP2) ---" + ColorUtils.ANSI_RESET);
                ViewUtils.println("Digita '/b' o '/back' per tornare indietro in qualsiasi momento.");
                ViewUtils.printSeparator();

                String promptProgetto = "Seleziona il progetto di cui vuoi modificare un canale:";
                int idProgetto = selezionaProgetto(mieiProgetti, promptProgetto);

                List<CanaleProgetto> canali = gestioneController.getCanaliDelProgetto(idProgetto);
                if (canali.isEmpty()) {
                    ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nAttenzione: Questo progetto non ha canali." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("Premi Invio per tornare alla selezione del progetto...");
                    continue;
                }

                while (true) {
                    int idCanale;
                    try {
                        String promptCanale = "Seleziona il canale a cui aggiungere un dipendente:";
                        idCanale = selezionaCanale(canali, promptCanale);
                    } catch (CommandException e) {
                        break;
                    }

                    List<Utente> dipendenti = gestioneController.getDipendentiAggiungibili(idCanale);
                    if (dipendenti.isEmpty()) {
                        ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nAttenzione: Non ci sono altri dipendenti da poter aggiungere a questo canale." + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue("Premi Invio per tornare alla selezione del canale...");
                        continue;
                    }

                    ViewUtils.println("\nSeleziona il dipendente da aggiungere al canale:");
                    dipendenti.forEach(d -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s %s", d.getIdUtente(), d.getNome(), d.getCognome())));
                    ViewUtils.printSeparator();
                    int idDipendente = InputHelper.chiediIdValido("ID Dipendente: ", dipendenti);

                    if (gestioneController.aggiungiDipendenteACanale(idCanale, idDipendente)) {
                        ViewUtils.println(ColorUtils.ANSI_GREEN + "\nDipendente aggiunto al canale con successo!" + ColorUtils.ANSI_RESET);
                    } else {
                        ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile aggiungere il dipendente." + ColorUtils.ANSI_RESET);
                    }

                    break progettoLoop;
                }
            }

        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata. Ritorno alla home...");
        }

        InputUtils.pressEnterToContinue("\nPremi Invio per tornare alla home...");
    }

    /**
     * Gestisce il flusso per rimuovere un Dipendente da un canale di un progetto.
     * Guida l'utente nella selezione del progetto, del canale e del dipendente da rimuovere,
     * con una navigazione che permette di tornare indietro a ogni passo.
     */
    private void handleRimuoviUtenteDaCanale() {
        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

        try {
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println("Non hai progetti da gestire.");
                InputUtils.pressEnterToContinue("");
                return;
            }

            progettoLoop:
            while (true) {
                ViewUtils.clearScreen();
                ViewUtils.println(ColorUtils.ANSI_BOLD + "--- RIMUOVI DIPENDENTE DA CANALE (CP3) ---" + ColorUtils.ANSI_RESET);
                ViewUtils.println("Digita '/b' o '/back' per tornare indietro in qualsiasi momento.");
                ViewUtils.printSeparator();

                String promptProgetto = "Seleziona il progetto da cui vuoi modificare un canale:";
                int idProgetto = selezionaProgetto(mieiProgetti, promptProgetto);
                ViewUtils.printSeparator();

                List<CanaleProgetto> canali = gestioneController.getCanaliDelProgetto(idProgetto);
                if (canali.isEmpty()) {
                    ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nAttenzione: Questo progetto non ha canali." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("Premi Invio per tornare alla selezione del progetto...");
                    continue;
                }

                while (true) {
                    int idCanale;
                    try {
                        String prompt = "Seleziona il canale da cui rimuovere un dipendente:";
                        idCanale = selezionaCanale(canali, prompt);
                    } catch (CommandException e) {
                        break;
                    }

                    List<Utente> dipendentiNelCanale = gestioneController.getDipendentiDelCanale(idCanale);
                    if (dipendentiNelCanale.isEmpty()) {
                        ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nAttenzione: Non ci sono dipendenti da poter rimuovere da questo canale." + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue("Premi Invio per tornare alla selezione del canale...");
                        continue;
                    }

                    ViewUtils.println("\nSeleziona il dipendente da rimuovere dal canale:");
                    dipendentiNelCanale.forEach(d -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s %s", d.getIdUtente(), d.getNome(), d.getCognome())));
                    ViewUtils.printSeparator();
                    int idDipendente = InputHelper.chiediIdValido("ID Dipendente: ", dipendentiNelCanale);

                    if (gestioneController.rimuoviDipendenteDaCanale(idCanale, idDipendente)) {
                        ViewUtils.println(ColorUtils.ANSI_GREEN + "\nDipendente rimosso dal canale con successo!" + ColorUtils.ANSI_RESET);
                    } else {
                        ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile rimuovere il dipendente." + ColorUtils.ANSI_RESET);
                    }

                    break progettoLoop;
                }
            }

        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata. Ritorno alla home...");
        }

        InputUtils.pressEnterToContinue("\nPremi Invio per tornare alla home...");
    }

    /**
     * Gestisce il flusso per la supervisione (sola lettura) delle chat private
     * originate da un progetto di cui l'utente è responsabile.
     */
    private void handleSupervisionaChatPrivate() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- SUPERVISIONE CHAT PRIVATE (CP4) ---" + ColorUtils.ANSI_RESET);
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

        try {
            // 1. Seleziona il progetto (riutilizziamo il nostro helper!)
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println("Non hai progetti da gestire.");
                InputUtils.pressEnterToContinue("");
                return;
            }
            String promptProgetto = "Seleziona il progetto di cui vuoi supervisionare le chat private:";
            int idProgetto = selezionaProgetto(mieiProgetti, promptProgetto);

            // 2. Chiama il controller per ottenere la lista di chat
            List<ChatSupervisioneDTO> chatDaSupervisionare = gestioneController.getChatPrivateDaSupervisionare(idProgetto, idUtenteLoggato);

            // 3. Gestisci i possibili risultati
            if (chatDaSupervisionare == null) {
                // Questo accade se la SP ha restituito un errore (es. non sei il responsabile)
                ViewUtils.println(ColorUtils.ANSI_RED + "ERRORE: Non sei autorizzato a visualizzare le chat di questo progetto o si è verificato un problema." + ColorUtils.ANSI_RESET);
            } else if (chatDaSupervisionare.isEmpty()) {
                ViewUtils.println("Non ci sono chat private originate da questo progetto da supervisionare.");
            } else {
                String nomeProgettoSelezionato = mieiProgetti.stream()
                        .filter(p -> p.getIdProgetto() == idProgetto)
                        .findFirst()
                        .map(Progetto::getNomeProgetto)
                        .orElse("ID: " + idProgetto); // Un fallback nel caso impossibile

                ViewUtils.println("\nElenco delle chat private originate dal progetto '" + nomeProgettoSelezionato + "':");
                ViewUtils.printSeparator();

                // Formattazione per una buona leggibilità
                String header = String.format("%-5s | %-20s | %-18s | %-18s | %s",
                        "ID", "Data Creazione", "Partecipanti", "Canale Origine", "Messaggio Origine");
                ViewUtils.println(ColorUtils.ANSI_BOLD + header + ColorUtils.ANSI_RESET);

                for (ChatSupervisioneDTO chat : chatDaSupervisionare) {
                    String partecipanti = chat.getCreatoreChatUsername() + " <-> " + chat.getPartecipanteChatUsername();
                    String riga = String.format("%-5d | %-20s | %-18s | %-18s | '%s'",
                            chat.getIdChat(),
                            chat.getDataCreazioneChat(),
                            partecipanti,
                            chat.getCanaleOrigineNome(),
                            chat.getMessaggioOrigineContenuto());
                    ViewUtils.println(riga);
                }

                // TODO: In futuro, qui chiederemo di selezionare una chat per vederne i dettagli.
            }

        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata.");
        }

        InputUtils.pressEnterToContinue("\nPremi Invio per tornare alla home...");
    }

    /**
     * Gestisce l'accesso alle aree di comunicazione dell'utente (canali e chat private).
     * Presenta un sotto-menu per permettere all'utente di scegliere dove interagire.
     */
    private void handleAccediACanaliEChat() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AREA COMUNICAZIONI ---" + ColorUtils.ANSI_RESET);
            ViewUtils.printSeparator();
            ViewUtils.println("1. Accedi ai Canali di Progetto");
            ViewUtils.println("2. Accedi alle Chat Private");
            ViewUtils.println("0. Torna alla Home Principale");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readInt("Seleziona un'opzione: ");

            try {
                switch (scelta) {
                    case 1:
                        handleAccessoCanaliProgetto();
                        break;
                    case 2:
                        handleAccessoChatPrivate();
                        break;
                    case 0:
                        return; // Esce dal metodo e torna al menu della home
                    default:
                        ViewUtils.println(ColorUtils.ANSI_RED + "Scelta non valida." + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue("");
                }
            } catch (CommandException e) {
                // Se l'utente preme /b in una delle sotto-funzioni,
                // l'eccezione viene "mangiata" qui e il loop del sotto-menu riparte.
                // Questo permette di tornare a questo menu, non alla home principale.
            }
        }
    }

    // In CapoProgettoView.java

    /**
     * Gestisce la selezione e l'accesso a un canale di progetto specifico.
     * Una volta selezionato un canale, lancia la ChatView per visualizzarne la conversazione.
     * @throws CommandException se l'utente sceglie di tornare indietro durante la selezione.
     */
    private void handleAccessoCanaliProgetto() throws CommandException {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- ACCESSO AI CANALI DI PROGETTO ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per tornare al menu precedente.");
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();
        List<CanaleProgetto> mieiCanali = interazioneController.getCanaliUtente(idUtenteLoggato);

        if (mieiCanali.isEmpty()) {
            ViewUtils.println("Non partecipi a nessun canale di progetto.");
            InputUtils.pressEnterToContinue("");
            return; // Termina il metodo se non ci sono canali
        }

        // Se siamo qui, ci sono canali da mostrare.
        // La gestione della CommandException viene fatta dal blocco try-catch nel metodo chiamante.
        String prompt = "Seleziona un canale per visualizzarne i messaggi:";
        int idCanaleSelezionato = selezionaCanale(mieiCanali, prompt);

        new ChatView(
                interazioneController,              // Il controller per le interazioni
                idCanaleSelezionato,                // L'ID del canale da visualizzare
                TipoContestoChat.CANALE_PROGETTO,   // Il tipo di contesto
                false,                              // 'solaLettura' è false, l'utente può interagire
                idUtenteLoggato                     // L'ID dell'utente che sta visualizzando
        ).show();

        // Dopo che l'utente esce dalla ChatView (con /b), il metodo termina.
        // Il controllo torna al loop del sottomenu in handleAccediACanaliEChat.
        // Non serve un "pressEnterToContinue" qui, perché il ritorno è gestito dalla navigazione.
    }

    /**
     * Gestisce l'accesso all'area delle chat private, presentando un ulteriore sotto-menu.
     * @throws CommandException se l'utente sceglie di tornare indietro.
     */
    private void handleAccessoChatPrivate() throws CommandException {
        // While (true) {
        //     Mostra sotto-menu: [1] Visualizza chat esistenti, [2] Avvia nuova chat, [0] Indietro
        //     switch (scelta) { ... }
        // }
        new StubView("Gestione Chat Private non ancora implementata.").show();
    }

    /**
     * Metodo helper per gestire la selezione di un progetto da parte dell'utente.
     * Mostra una lista di progetti e chiede di inserire un ID valido.
     *
     * @param progetti La lista di {@link Progetto} da cui scegliere.
     * @param prompt Il messaggio da mostrare all'utente prima della lista.
     * @return L'ID del progetto selezionato.
     * @throws CommandException se l'utente inserisce un comando per tornare indietro (es. /b).
     */
    private int selezionaProgetto(List<Progetto> progetti, String prompt) throws CommandException {
        ViewUtils.println(prompt);
        progetti.forEach(p -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s", p.getIdProgetto(), p.getNomeProgetto())));
        ViewUtils.printSeparator();
        return InputHelper.chiediIdValido("ID Progetto: ", progetti);
    }

    /**
     * Metodo helper per gestire la selezione di un canale da parte dell'utente.
     * Mostra una lista di canali e chiede di inserire un ID valido.
     *
     * @param canali La lista di {@link CanaleProgetto} da cui scegliere.
     * @param prompt Il messaggio da mostrare all'utente prima della lista (es. "Seleziona il canale a cui aggiungere...").
     * @return L'ID del canale selezionato.
     * @throws CommandException se l'utente inserisce un comando per tornare indietro (es. /b).
     */
    private int selezionaCanale(List<CanaleProgetto> canali, String prompt) throws CommandException {
        ViewUtils.println("\n" + prompt);
        canali.forEach(c -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s", c.getIdCanale(), c.getNomeCanale())));
        ViewUtils.printSeparator();
        return InputHelper.chiediIdValido("ID Canale: ", canali);
    }
}
