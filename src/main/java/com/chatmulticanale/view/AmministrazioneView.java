package com.chatmulticanale.view;

import com.chatmulticanale.controller.AmministrazioneController;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.Progetto; // <-- NUOVO IMPORT
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.ColorUtils;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.SessionManager;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;
import java.util.List;

/**
 * Rappresenta la schermata principale (Home) per un utente con ruolo Amministratore.
 * Fornisce accesso a tutte le funzionalità di gestione del sistema.
 */
public class AmministrazioneView implements View {
    private final AmministrazioneController adminController;

    public AmministrazioneView(AmministrazioneController controller) {
        this.adminController = controller;
    }

    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- HOME AMMINISTRATORE ---" + ColorUtils.ANSI_RESET);
            ViewUtils.println("Benvenuto, " + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();
            ViewUtils.println("1. Promuovi un utente a Capo Progetto (AM1)");
            ViewUtils.println("2. Rimuovi ruolo Capo Progetto (AM2)");
            ViewUtils.println("3. Assegna responsabilità Progetto a un Capo Progetto (AM3)");
            ViewUtils.println("4. Riassegna responsabilità Progetto (AM4)");
            ViewUtils.println("5. Aggiungi progetto");
            ViewUtils.println("0. Logout");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readInt("Seleziona un'opzione: ");
            switch (scelta) {
                case 1:
                    handlePromuoviUtente();
                    break;
                case 2:
                    // Attiviamo la nuova funzionalità complessa
                    handleDegradaCapoProgetto();
                    break;
                case 3:
                    handleAssegnaResponsabilita();
                    break;
                case 4:
                    new StubView("Funzionalità non ancora implementata.").show();
                    break;
                case 5:
                    handleAggiungiProgetto();
                    break;
                case 0:
                    SessionManager.getInstance().logout();
                    return Navigazione.logout();
                default:
                    ViewUtils.println(ColorUtils.ANSI_RED + "Scelta non valida. Riprova." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("");
            }
        }
    }

    private void handlePromuoviUtente() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- PROMUOVI UTENTE A CAPO PROGETTO ---" + ColorUtils.ANSI_RESET);

        List<Utente> dipendenti = adminController.getListaUtentiPromuovibili();

        if (dipendenti.isEmpty()) {
            ViewUtils.println("Nessun dipendente da promuovere al momento.");
        } else {
            ViewUtils.println("Lista dei Dipendenti che possono essere promossi:");
            ViewUtils.printSeparator();
            for (Utente utente : dipendenti) {
                // Ho corretto getNome() in getNomeUtente() per coerenza con il DAO
                String rigaUtente = String.format("ID: %-5d | Nome: %-15s | Cognome: %s",
                        utente.getIdUtente(),
                        utente.getNome(),
                        utente.getCognome());
                ViewUtils.println(rigaUtente);
            }
            ViewUtils.printSeparator();

            int idUtente = InputUtils.readInt("Inserisci l'ID dell'utente da promuovere (o '0' per annullare): ");

            if (idUtente != 0) {
                // Qui dovremmo aggiungere un controllo per verificare che l'ID inserito sia valido
                boolean successo = adminController.promuoviUtenteACapoProgetto(idUtente);
                if (successo) {
                    ViewUtils.println(ColorUtils.ANSI_GREEN + "\nUtente promosso con successo!" + ColorUtils.ANSI_RESET);
                } else {
                    ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile promuovere l'utente." + ColorUtils.ANSI_RESET);
                }
            } else {
                ViewUtils.println("Operazione annullata.");
            }
        }
        InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
    }

    /**
     * Gestisce il flusso guidato e sicuro per la rimozione del ruolo di Capo Progetto,
     * forzando la riassegnazione delle responsabilità come da regola aziendale RA3.
     */
    private void handleDegradaCapoProgetto() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- RIMUOVI RUOLO CAPO PROGETTO (Procedura Guidata) ---" + ColorUtils.ANSI_RESET);

        // 1. Chiedi quale Capo Progetto degradare
        List<Utente> capiProgetto = adminController.getListaCapiProgetto();
        if (capiProgetto.isEmpty()) {
            ViewUtils.println("Nessun Capo Progetto da rimuovere.");
            InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
            return;
        }
        ViewUtils.println("Lista dei Capi Progetto attuali:");
        capiProgetto.forEach(u -> ViewUtils.println(String.format("ID: %-5d | Nome: %-15s | Cognome: %s", u.getIdUtente(), u.getNome(), u.getCognome())));
        ViewUtils.printSeparator();
        int idDaDegradare = InputUtils.readInt("Inserisci l'ID del Capo Progetto da degradare (o '0' per annullare): ");
        if (idDaDegradare == 0) return;

        // 2. Controlla se ci sono altri Capi Progetto disponibili per la riassegnazione
        final int finalIdDaDegradare = idDaDegradare;
        List<Utente> altriCapi = capiProgetto.stream()
                .filter(cp -> cp.getIdUtente() != finalIdDaDegradare)
                .toList();

        if (altriCapi.isEmpty()) {
            ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile procedere. Non ci sono altri Capi Progetto a cui riassegnare i progetti." + ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
            return;
        }

        // 3. Trova e gestisci i progetti dell'utente da degradare
        List<Progetto> progettiDaRiassegnare = adminController.getProgettiDiUnResponsabile(idDaDegradare);
        if (!progettiDaRiassegnare.isEmpty()) {
            ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nATTENZIONE: Questo utente è responsabile dei seguenti progetti." + ColorUtils.ANSI_RESET);
            ViewUtils.println("È necessario riassegnarli tutti prima di poter procedere con il degrado.");

            for (Progetto progetto : progettiDaRiassegnare) {
                ViewUtils.printSeparator();
                ViewUtils.println("Riassegnazione del progetto: '" + progetto.getNomeProgetto() + "' (ID: " + progetto.getIdProgetto() + ")");
                ViewUtils.println("\nCapi Progetto disponibili per la riassegnazione:");
                altriCapi.forEach(u -> ViewUtils.println(String.format("ID: %-5d | Nome: %-15s", u.getIdUtente(), u.getNome())));

                int idNuovoResponsabile = -1;
                boolean idValido = false;
                while (!idValido) {
                    idNuovoResponsabile = InputUtils.readInt("\nInserisci l'ID del nuovo responsabile per questo progetto: ");
                    final int finalIdNuovoResponsabile = idNuovoResponsabile;
                    idValido = altriCapi.stream().anyMatch(cp -> cp.getIdUtente() == finalIdNuovoResponsabile);
                    if (!idValido) {
                        ViewUtils.println(ColorUtils.ANSI_RED + "ID non valido. Seleziona un ID dalla lista." + ColorUtils.ANSI_RESET);
                    }
                }

                if (adminController.riassegnaProgetto(progetto.getIdProgetto(), idNuovoResponsabile)) {
                    ViewUtils.println(ColorUtils.ANSI_GREEN + "Progetto riassegnato con successo." + ColorUtils.ANSI_RESET);
                } else {
                    ViewUtils.println(ColorUtils.ANSI_RED + "ERRORE durante la riassegnazione. Procedura annullata." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("");
                    return; // Interrompe l'intera operazione se una riassegnazione fallisce
                }
            }
            ViewUtils.println(ColorUtils.ANSI_GREEN + "\nTutte le responsabilità sono state trasferite con successo." + ColorUtils.ANSI_RESET);
        } else {
            ViewUtils.println("\nL'utente selezionato non ha responsabilità di progetto attive.");
        }

        // 4. Esegui il degrado finale solo se tutti i passaggi precedenti sono andati a buon fine
        ViewUtils.println("Procedo con il degrado del ruolo...");
        if (adminController.finalizzaDegradoCapoProgetto(idDaDegradare)) {
            ViewUtils.println(ColorUtils.ANSI_GREEN + "Ruolo rimosso con successo! L'utente è ora un Dipendente." + ColorUtils.ANSI_RESET);
        } else {
            ViewUtils.println(ColorUtils.ANSI_RED + "ERRORE finale durante il degrado del ruolo." + ColorUtils.ANSI_RESET);
        }

        InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
    }

    /**
     * Gestisce il flusso per l'aggiunta di un nuovo progetto.
     */
    private void handleAggiungiProgetto() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AGGIUNGI PROGETTO ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per annullare.");
        ViewUtils.printSeparator();

        try {
            String nomeProgetto = InputUtils.askForInput("Nome del nuovo progetto: ");
            String descrizioneProgetto = InputUtils.askForInput("Descrizione del progetto: ");

            if (adminController.creaNuovoProgetto(nomeProgetto, descrizioneProgetto)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nProgetto '" + nomeProgetto + "' creato con successo!" + ColorUtils.ANSI_RESET);
                ViewUtils.println("Ora puoi assegnarlo a un Capo Progetto usando l'opzione 3.");
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile creare il progetto. Il nome potrebbe essere già esistente." + ColorUtils.ANSI_RESET);
            }
        } catch (CommandException e) {
            // Se l'utente digita un comando, l'eccezione viene gestita qui
            // e semplicemente si torna al menu precedente, perché il NavigationManager
            // non viene coinvolto. Mostriamo un feedback.
            ViewUtils.println("\nOperazione annullata.");
        }

        InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
    }

    /**
     * Gestisce il flusso per assegnare un progetto non assegnato a un Capo Progetto.
     */
    private void handleAssegnaResponsabilita() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- ASSEGNA RESPONSABILITÀ PROGETTO ---" + ColorUtils.ANSI_RESET);

        // 1. Recupera e mostra i progetti non assegnati
        List<Progetto> progetti = adminController.getListaProgettiNonAssegnati();
        if (progetti.isEmpty()) {
            ViewUtils.println("Nessun progetto da assegnare al momento.");
            InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
            return;
        }

        // 2. Recupera e mostra i Capi Progetto disponibili
        List<Utente> capiProgetto = adminController.getListaCapiProgetto();
        if (capiProgetto.isEmpty()) {
            ViewUtils.println("Nessun Capo Progetto disponibile a cui assegnare progetti.");
            InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
            return;
        }

        try {
            ViewUtils.println("Lista dei Progetti non assegnati:");
            progetti.forEach(p -> ViewUtils.println(String.format("ID: %-5d | Nome: %s", p.getIdProgetto(), p.getNomeProgetto())));
            ViewUtils.printSeparator();
            String idProgettoStr = InputUtils.askForInput("Inserisci l'ID del progetto da assegnare: ");
            int idProgetto = Integer.parseInt(idProgettoStr);

            ViewUtils.println("\nLista dei Capi Progetto disponibili:");
            capiProgetto.forEach(cp -> ViewUtils.println(String.format("ID: %-5d | Nome: %s %s", cp.getIdUtente(), cp.getNome(), cp.getCognome())));
            ViewUtils.printSeparator();
            String idCapoStr = InputUtils.askForInput("Inserisci l'ID del Capo Progetto a cui assegnare il progetto: ");
            int idCapo = Integer.parseInt(idCapoStr);

            // TODO: Aggiungere controlli per verificare che gli ID inseriti siano validi

            if (adminController.assegnaProgetto(idProgetto, idCapo)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nProgetto assegnato con successo!" + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile assegnare il progetto." + ColorUtils.ANSI_RESET);
            }

        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata.");
        } catch (NumberFormatException e) {
            ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Inserisci un ID numerico valido." + ColorUtils.ANSI_RESET);
        }

        InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
    }
}
