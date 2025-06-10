package com.chatmulticanale.view.navigation;

/**
 * Rappresenta un'interfaccia generica per qualsiasi "schermata" o "vista"
 * che possa essere mostrata all'utente all'interno del ciclo di navigazione principale.
 * Ogni vista, quando viene eseguita, deve comunicare al sistema quale azione
 * di navigazione intraprendere successivamente.
 */
public interface View {

    /**
     * Mostra la vista all'utente, gestisce le sue interazioni e, alla fine,
     * restituisce un oggetto Navigazione che descrive la prossima mossa.
     *
     * @return Un oggetto {@link Navigazione} che indica al NavigationManager cosa fare dopo.
     */
    Navigazione show();
}
