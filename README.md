# DGLOGIN

This is a login template-template for [Niagara AX](https://www.tridium.com/en/products-services/niagaraax).  After the Niagara security changes, a generic login template that serves content from the file system does not seem to work.  The intent of this module is for implements to rename things and build their own custom module.

Requirements
------------

Niagara 3.8, post-security changes.


Creating a Custom Module
------------------------

Make a copy of this template.

1.  Find and replace all instances of "dglogin" in all files.
2.  You don't have to, but you can do the same for "DGLogin".
3.  Modify src/rsc/index.html.  The index.html here is bare bones and is
    a good starting point.  See the class header / bajadoc for
    javax.baja.web.BLoginTmeplate for more details.
4.  If you use another directory besides src/rsc/assets be sure to add that in build.xml.

Tokens
------
There are several tokens that can be in index.html that will be substituted with 
dynamic values when served to the client.  Tokens take the form <?TOKEN\_NAME?>.

  - ABS\_PATH\_BASE - Base uri.  Do not use this to refererence additional content 
    in index.html, use CONTENT\_PATH instead.
  - AUTO\_LOGIN - Will be true or false.  Determines whether or not to show
    the remember me checkbox.
  - COOKIE\_POSTFIX - Niagara uses this for cookie stuff.
  - CONTENT\_PATH - Base uri for additional content placed under src/rsc.  It 
    will end with a slash. Equivalent to <?ABS\_PATH\_BASE?>/login/dg/
  - MESSAGE - Informational and error messages for the user.  Will be an empty
    string where there is no message.
  - SCHEME - The authentication scheme used by Niagara.
  - STATION\_NAME - Yup.
  - WEB\_VERSION - The version of Tridium's web module.  You should append this
    to Tridium's javascript file links as a query string for caching reasons.

History
-------
_1.0.0 - 2016-10-20_
  - Hello World!
