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
*	- authentification Google OAuth2 (les mots de passe Google ne sont plus utilis�s et ne passent plus en clair sur le r�seau) 
*	- nouvelles fonctionnalit�s (couleurs sur �v�nements, rappels, propri�t�s �tendues)
*	- modification interface pour manipuler les agendas avec leur nom au lieu de leur ID Google (plus simple pour un humain :-)
* synchronisation incr�mentale, les �v�nements sont mis � jour s'ils existent d�j� dans l'agenda, au lieu d'�tre supprim�s et recr��s
****************************************
 0.4 ?? (En cours)
 * migration vers scheduler Java Quartz (affranchissement base de donn�es pour ex�cution jobs)
 ****************************************