/*global angular*/

(function () {
	'use strict';

	angular.module('translateConfig', ['pascalprecht.translate'])

        .config(['$translateProvider', function ($translateProvider) {

            $translateProvider.useSanitizeValueStrategy('escaped');

            $translateProvider.translations('cs', {

                //SITE TITLES:
                'TITLE_LOGIN': 'Přihlášení',
                'TITLE_ABOUT': 'O aplikaci',
                'TITLE_NOT_FOUND': 'Stránka nenalezena',
                'TITLE_LOGOUT': 'Odhlášení',
                'TITLE_SETTINGS': 'Nastavení',
                'TITLE_EXAMPLE': 'Příklad',

                //SITE HEADERS:
                'MAIN_HEADER': 'Automaster Open Work Orders Reporting',
                'MAIN_HEADER_SHORT': 'AMOWOR',
                'LOGIN_HEADER': 'Přihlášení',
                'LOGOUT_HEADER': 'Odhlašování',
                'NOT_FOUND_HEADER': 'Error 404: Stránka nenalezena',

                //MENU:
                'MENU_ABOUT': 'O aplikaci',
                'MENU_ACCESS': 'Oprávnění',
                'MENU_LOGIN': 'Přihlášení',
                'MENU_LOGOUT': 'Odhlásit',
                'MENU_SETTINGS': 'Nastavení',
                'MENU_OPENWO': 'Otevřené zakázky',

                //NOT FOUND PAGE:
                'NOT_FOUND_INFO_TEXT': 'Požadovaný dokument na serveru neexistuje.',

                //LOGIN PAGE:
                'LOGIN_USERNAME': 'Jméno',
                'LOGIN_PASSWORD': 'Heslo',
                'LOGIN_BUTTON': 'Přihlásit',

                //ABOUT PAGE:
                'ABOUT_INFO': 'Aplikace je aktuálně ve verzi',

                //RESPONSE:
                'RESPONSE_TOKEN_EXPIRED': 'Your session expired.',
                'RESPONSE_TOKEN_WRONG': 'Vaše přihlášení není platné.',
                'RESPONSE_TOKEN_UNAUTHORIZED': 'Nepovolený přístup.',
                'RESPONSE_EXCEPTION_OCCURRED': 'Omlouváme se. Na serveru se vyskytla chyba.',
                'RESPONSE_POST_FILE_ERROR': 'Soubor se nepodařilo nahrát.',
                'RESPONSE_REQUEST_NOT_FOUND': 'Požadavek s daným ID nenalezen. Zkuste stránku načíst znovu.',
                'RESPONSE_UNAUTHORIZED': 'Špatné přihlašovací údaje.',
                'RESPONSE_NO_ROLE': 'Nemáte nastavené oprávnění pro přístup do aplikace.',
                'RESPONSE_LOGIN_ERROR': 'Chyba přihlášení v login modulu.'
            });

            $translateProvider.translations('en', {
                //SITE TITLES:
                'TITLE_LOGIN': 'Login',
                'TITLE_ABOUT': 'About',
                'TITLE_NOT_FOUND': 'Page not found',
                'TITLE_LOGOUT': 'Logout',
                'TITLE_SETTINGS': 'Settings',
                'TITLE_EXAMPLE': 'Example',

                //SITE HEADERS:
                'MAIN_HEADER': 'TempWeb',
                'MAIN_HEADER_SHORT': 'TW',
                'LOGIN_HEADER': 'Login',
                'LOGOUT_HEADER': 'Logout',
                'NOT_FOUND_HEADER': 'Error 404: Page not found',

                //MENU:
                'MENU_ABOUT': 'About',
                'MENU_ACCESS': 'Access',
                'MENU_LOGIN': 'Login',
                'MENU_LOGOUT': 'Logout',
                'MENU_SETTINGS': 'Settings',

                //NOT FOUND PAGE:
                'NOT_FOUND_INFO_TEXT': 'Requested document not found.',

                //LOGIN PAGE:
                'LOGIN_USERNAME': 'Name',
                'LOGIN_PASSWORD': 'Password',
                'LOGIN_BUTTON': 'Login',

                //ABOUT PAGE:
                'ABOUT_INFO': 'Application version is'
            });


            $translateProvider.translations('de', {

            });

            $translateProvider.preferredLanguage('cs');
		
        }]);
}());