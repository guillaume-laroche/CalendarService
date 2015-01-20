Versions 
****************************************
0.1 - 2011 
* Synchronisation en mode console
* API GData v2 
****************************************
0.2 - 2012
* ajout interface web de pilotage des synchronisations manuelles
* utilisation scheduler Oracle pour soumettre les jobs de synchronisation
****************************************
0.3 - 03/04/2014
* migration vers API v3, qui inclut :
*	- authentification Google OAuth2 (les mots de passe Google ne sont plus utilisés et ne passent plus en clair sur le réseau) 
*	- nouvelles fonctionnalités (couleurs sur événements, rappels, propriétés étendues)
*	- modification interface pour manipuler les agendas avec leur nom au lieu de leur ID Google (plus simple pour un humain :-)
* synchronisation incrémentale, les évènements sont mis à jour s'ils existent déjà dans l'agenda, au lieu d'être supprimés et recréés
****************************************
 0.4 ?? (En cours)
 * migration vers scheduler Java Quartz (affranchissement base de données pour exécution jobs)
 ****************************************