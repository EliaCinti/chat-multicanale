package com.chatmulticanale.view;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.TipoContestoChat;
import com.chatmulticanale.utils.ColorUtils;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChatView {

    private final InterazioneUtenteController controller;
    private final int idContesto;
    private final TipoContestoChat tipoContesto;
    private final boolean solaLettura;
    private final int idUtenteLoggato;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public ChatView(InterazioneUtenteController controller, int idContesto, TipoContestoChat tipoContesto, boolean solaLettura, int idUtenteLoggato) {
        this.controller = controller;
        this.idContesto = idContesto;
        this.tipoContesto = tipoContesto;
        this.solaLettura = solaLettura;
        this.idUtenteLoggato = idUtenteLoggato;
    }

    public void show() {
        int paginaCorrente = 1;

        while (true) {
            ViewUtils.clearScreen();

            // Recupera i messaggi in base al contesto
            List<MessaggioDTO> messaggi;
            if (tipoContesto == TipoContestoChat.CANALE_PROGETTO) {
                messaggi = controller.getPaginaMessaggiCanale(idContesto, this.idUtenteLoggato, paginaCorrente);
            } else { // CHAT_PRIVATA
                messaggi = controller.getPaginaMessaggiChatPrivata(idContesto, this.idUtenteLoggato, paginaCorrente);
            }

            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- Conversazione (Pagina " + paginaCorrente + ") ---" + ColorUtils.ANSI_RESET);
            if (solaLettura) {
                ViewUtils.println(ColorUtils.ANSI_YELLOW + " (Modalità Sola Lettura)" + ColorUtils.ANSI_RESET);
            }
            ViewUtils.printSeparator();

            if (messaggi.isEmpty() && paginaCorrente > 1) {
                ViewUtils.println("Non ci sono altre pagine.");
                paginaCorrente--;
                InputUtils.pressEnterToContinue("Premi Invio per continuare...");
                continue;
            } else if (messaggi.isEmpty()) {
                ViewUtils.println("Nessun messaggio in questa conversazione.");
            } else {
                for (MessaggioDTO msg : messaggi) {
                    // Visualizziamo anche l'ID del messaggio, servirà per la citazione
                    String riga = String.format("ID %-4d | [%s] %s%s%s: %s",
                            msg.getIdMessaggio(),
                            dateFormat.format(msg.getTimestamp()),
                            ColorUtils.ANSI_BOLD,
                            msg.getUsernameMittente(),
                            ColorUtils.ANSI_RESET,
                            msg.getContenuto());
                    ViewUtils.println(riga);
                }
            }

            ViewUtils.printSeparator();
            ViewUtils.print("Opzioni: [N] Pag. Succ. | [P] Pag. Prec.");
            if (!solaLettura) {
                ViewUtils.print(" | [I] Invia | [C] Cita");
                // L'opzione "Avvia Chat" è disponibile solo se siamo in un canale
                if (tipoContesto == TipoContestoChat.CANALE_PROGETTO) {
                    ViewUtils.print(" | [A] Avvia Chat Privata");
                }
            }
            ViewUtils.println(" | [/b] Indietro");

            try {
                String scelta = InputUtils.askForInput("Comando: ").toLowerCase();
                switch (scelta) {
                    case "n":
                        paginaCorrente++;
                        break;
                    case "p":
                        if (paginaCorrente > 1) {
                            paginaCorrente--;
                        } else {
                            ViewUtils.println("Sei già alla prima pagina.");
                            InputUtils.pressEnterToContinue("");
                        }
                        break;
                    case "i":
                        if (!solaLettura) {
                            handleInviaMessaggio(null); // Messaggio semplice
                        }
                        break;
                    case "c":
                        if (!solaLettura) {
                            handleCitaMessaggio(messaggi); // Messaggio con citazione
                        }
                        break;
                    case "a":
                        if (!solaLettura && tipoContesto == TipoContestoChat.CANALE_PROGETTO) {
                            handleAvviaChatPrivata(messaggi);
                        }
                        break;
                    default:
                        ViewUtils.println(ColorUtils.ANSI_RED + "ERRORE: Comando non riconosciuto." + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue("Premi Invio per riprovare...");
                        break; // Ignora input non validi
                }
            } catch (CommandException e) {
                return; // Esce dal metodo e torna al chiamante
            }
        }
    }

    /**
     * Gestisce il flusso per l'invio di un nuovo messaggio, con o senza citazione.
     *
     * @param idMessaggioCitato L'ID del messaggio da citare. Può essere null per un messaggio semplice.
     */
    private void handleInviaMessaggio(Integer idMessaggioCitato) throws CommandException {
        ViewUtils.printSeparator();

        String prompt = idMessaggioCitato == null ?
                "Scrivi il tuo messaggio (o /b per annullare): " :
                "Scrivi la tua risposta al messaggio " + idMessaggioCitato + " (o /b per annullare): ";

        String contenuto = InputUtils.askForInput(prompt);

        boolean successo;
        if (tipoContesto == TipoContestoChat.CANALE_PROGETTO) {
            if (idMessaggioCitato == null) {
                successo = controller.inviaMessaggioCanale(this.idContesto, this.idUtenteLoggato, contenuto);
            } else {
                successo = controller.inviaMessaggioConCitazioneCanale(this.idContesto, this.idUtenteLoggato, contenuto, idMessaggioCitato);
            }
        } else { // CHAT_PRIVATA
            if (idMessaggioCitato == null) {
                successo = controller.inviaMessaggioChat(this.idContesto, this.idUtenteLoggato, contenuto);
            } else {
                successo = controller.inviaMessaggioConCitazioneChat(this.idContesto, this.idUtenteLoggato, contenuto, idMessaggioCitato);
            }
        }

        if (successo) {
            ViewUtils.println(ColorUtils.ANSI_GREEN + "Messaggio inviato!" + ColorUtils.ANSI_RESET);
        } else {
            ViewUtils.println(ColorUtils.ANSI_RED + "Errore: impossibile inviare il messaggio." + ColorUtils.ANSI_RESET);
        }
        InputUtils.pressEnterToContinue("Premi Invio per continuare...");
    }

    /**
     * Gestisce la logica per la selezione di un messaggio da citare.
     *
     * @param messaggiVisibili La lista dei messaggi attualmente visibili a schermo.
     */
    private void handleCitaMessaggio(List<MessaggioDTO> messaggiVisibili) {
        if (messaggiVisibili.isEmpty()) {
            ViewUtils.println(ColorUtils.ANSI_YELLOW + "Non ci sono messaggi in questa pagina da poter citare." + ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue("");
            return;
        }

        try {
            int idDaCitare = InputUtils.readInt("Inserisci l'ID del messaggio da citare: ");

            // Validazione manuale: controlla se l'ID inserito è presente nella lista.
            boolean idValido = messaggiVisibili.stream()
                    .anyMatch(m -> m.getIdMessaggio() == idDaCitare);

            if (idValido) {
                // Se l'ID è valido, procediamo con l'invio del messaggio di risposta.
                handleInviaMessaggio(idDaCitare);
            } else {
                // Se l'ID non è nella lista, informiamo l'utente.
                ViewUtils.println(ColorUtils.ANSI_RED + "ID non valido. Puoi citare solo i messaggi presenti in questa pagina." + ColorUtils.ANSI_RESET);
                InputUtils.pressEnterToContinue("");
            }
        } catch (CommandException e) {
            // Gestisce il caso in cui l'utente digiti /b o /back.
            ViewUtils.println("Citazione annullata.");
            InputUtils.pressEnterToContinue("");
        }
    }

    /**
     * Gestisce il flusso per avviare una nuova chat privata a partire da un
     * messaggio visibile nella conversazione attuale.
     *
     * @param messaggiVisibili La lista dei messaggi attualmente mostrati a schermo.
     */
    private void handleAvviaChatPrivata(List<MessaggioDTO> messaggiVisibili) {
        if (messaggiVisibili.isEmpty()) {
            ViewUtils.println(ColorUtils.ANSI_YELLOW + "Non ci sono messaggi in questa pagina da cui partire." + ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue("");
            return;
        }

        try {
            int idMessaggioOrigine = InputUtils.readInt("Inserisci l'ID del messaggio da cui avviare la chat privata: ");

            // Validazione: l'utente può avviare una chat solo da un messaggio che vede.
            boolean idValido = messaggiVisibili.stream()
                    .anyMatch(m -> m.getIdMessaggio() == idMessaggioOrigine);

            if (idValido) {
                java.util.Optional<String> errorOptional = controller.avviaNuovaChatPrivata(idMessaggioOrigine, this.idUtenteLoggato);

                if (errorOptional.isEmpty()) { // Successo
                    ViewUtils.println(ColorUtils.ANSI_GREEN + "\nChat privata avviata con successo!" + ColorUtils.ANSI_RESET);
                    ViewUtils.println("Ora puoi accedervi dalla sezione 'Chat Private' nella home.");
                } else { // Errore
                    ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile avviare la chat." + ColorUtils.ANSI_RESET);
                    ViewUtils.println(ColorUtils.ANSI_RED + "Motivo: " + errorOptional.get() + ColorUtils.ANSI_RESET);
                }
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "ID non valido. Puoi avviare una chat solo da un messaggio presente in questa pagina." + ColorUtils.ANSI_RESET);
            }
            InputUtils.pressEnterToContinue("");

        } catch (CommandException e) {
            // Gestisce solo l'annullamento tramite /b o /back.
            ViewUtils.println("Operazione annullata.");
            InputUtils.pressEnterToContinue("");
        }
    }
}