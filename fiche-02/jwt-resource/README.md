# RESTful JAX-RS Application : Gestion securisee (JWT) de donnees de films

## RESTful API : operations disponibles

### Operations associees a  la gestion des utilisateurs et l'authentification

<br>
<table style="caption-side: top">
<caption>Operations sur les ressources de type "Authentification"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Auths?</th>
    <th>Operation</th>
</tr>

<tr>
    <td>auths/login</td>
    <td>POST</td>
    <td>Non</td>
    <td>
    Verifier les « credentials » d un User et renvoyer le User et un token JWT si ils sont OK
    </td>
</tr>
<tr>
    <td>auths/register</td>
    <td>POST</td>
    <td>Non</td>
    <td>
    Creer une ressource User et un token JWT et les renvoyer
    </td>
</tr>

</table>

<br>

<table style="caption-side: top">
<caption>Operations sur les ressources de type "User"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Auths?</th>
    <th>Operation</th>
</tr>

<tr>
    <td>users/init</td>
    <td>POST</td>
    <td>Non</td>
    <td>
    CREATE ONE : Creer une ressource basee sur des donnees par defaut (login = "james",password= "password")
    </td>
</tr>
<tr>
    <td>users/me</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    READ ONE : Lire la ressource identifiee par le biais du token donne dans le header de la requete
    </td>
</tr>

</table>

### Operations associees a la gestion des pages

<table style="caption-side: top">
<caption>Operations sur les ressources de type "Page"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Auths?</th>
    <th>Operation</th>
</tr>

<tr>
    <td>pages</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    READ ALL : Lire toutes les ressources identifiees
    </td>
</tr>

<tr>
    <td>pages/{id}</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    READ ONE : Lire la ressource identifiee
    </td>
</tr>

<tr>
    <td>pages</td>
    <td>POST</td>
    <td>JWT</td>
    <td>
    CREATE ONE : Creer une ressource basee sur les donnees de la requete
    </td>
</tr>

<tr>
    <td>pages/{id}</td>
    <td>DELETE</td>
    <td>JWT</td>
    <td>
    DELETE ONE : Effacer la ressource identifiee
    </td>
</tr>

<tr>
    <td>pages/{id}</td>
    <td>PUT</td>
    <td>JWT</td>
    <td>
    UPDATE ONE : Replacer l'entierete de la ressource par les donnees de la requete
    </td>
</tr>


</table>