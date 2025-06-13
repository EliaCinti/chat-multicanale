package com.chatmulticanale.view;

import com.chatmulticanale.controller.GestioneProgettiController;
import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.Progetto;
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
                    // handleRimuoviUtenteDaCanale();
                    new StubView().show();
                    break;
                case 5:
                    // handleSupervisionaChatPrivate();
                    new StubView().show();
                    break;
                case 6:
                    // handleAccediACanaliEChat();
                    new StubView().show();
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

    /**
     * Gestisce il flusso per aggiungere un Dipendente a un canale di un progetto.
     * Il flusso guida il Capo Progetto attraverso la selezione del progetto,
     * del canale e infine del dipendente da aggiungere. Permette di tentare più volte
     * la selezione del progetto se il primo tentativo non ha canali disponibili.
     */
    private void handleAggiungiUtenteACanale() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AGGIUNGI DIPENDENTE A CANALE (CP2) ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per annullare in qualsiasi momento.");
        ViewUtils.printSeparator();

        int idUtenteLoggato = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

        try {
            // Otteniamo la lista dei progetti una sola volta
            List<Progetto> mieiProgetti = gestioneController.getProgettiDiCuiSonoResponsabile(idUtenteLoggato);
            if (mieiProgetti.isEmpty()) {
                ViewUtils.println("Non hai progetti da gestire.");
                InputUtils.pressEnterToContinue("");
                return; // Esce se non ci sono proprio progetti
            }

            int idProgetto;
            List<CanaleProgetto> canali;

            // --- INIZIO NUOVA LOGICA: LOOP DI SELEZIONE ---
            while (true) {
                // 1. Seleziona il progetto
                ViewUtils.println("Seleziona il progetto di cui vuoi modificare un canale:");
                mieiProgetti.forEach(p -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s", p.getIdProgetto(), p.getNomeProgetto())));
                ViewUtils.printSeparator();
                idProgetto = InputHelper.chiediIdValido("ID Progetto: ", mieiProgetti);

                // 2. Controlla se il progetto selezionato ha canali
                canali = gestioneController.getCanaliDelProgetto(idProgetto);

                if (canali.isEmpty()) {
                    ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nAttenzione: Questo progetto non ha ancora nessun canale. Per favore, scegline un altro." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("Premi Invio per continuare...");
                    ViewUtils.clearScreen(); // Pulisce per la nuova selezione
                } else {
                    break; // Esce dal loop perché abbiamo trovato un progetto con canali
                }
            }

            // 3. Seleziona il canale di quel progetto
            ViewUtils.println("\nSeleziona il canale a cui aggiungere un dipendente:");
            canali.forEach(c -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s", c.getIdCanale(), c.getNomeCanale())));
            ViewUtils.printSeparator();
            int idCanale = InputHelper.chiediIdValido("ID Canale: ", canali);

            // 4. Seleziona il dipendente da aggiungere
            List<Utente> dipendenti = gestioneController.getDipendentiAggiungibili(idCanale);
            if (dipendenti.isEmpty()) {
                ViewUtils.println("\nNon ci sono altri dipendenti da poter aggiungere a questo canale.");
                InputUtils.pressEnterToContinue("");
                return;
            }
            ViewUtils.println("\nSeleziona il dipendente da aggiungere al canale:");
            dipendenti.forEach(d -> ViewUtils.println(String.format("  ID: %-5d | Nome: %s %s", d.getIdUtente(), d.getNome(), d.getCognome())));
            int idDipendente = InputHelper.chiediIdValido("ID Dipendente: ", dipendenti);

            // 5. Esegui l'operazione
            if (gestioneController.aggiungiDipendenteACanale(idCanale, idDipendente)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nDipendente aggiunto al canale con successo!" + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile aggiungere il dipendente." + ColorUtils.ANSI_RESET);
            }

        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata.");
        }

        InputUtils.pressEnterToContinue("Premi Invio per tornare alla home...");
    }
}
