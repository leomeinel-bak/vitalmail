<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![Quality][quality-shield]][quality-url]

<!-- PROJECT LOGO -->
<!--suppress ALL -->
<br />
<p align="center">
  <a href="https://github.com/LeoMeinel/VitalMail">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">VitalMail</h3>

  <p align="center">
    Send mail on Spigot and Paper
    <br />
    <a href="https://github.com/LeoMeinel/VitalMail"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/LeoMeinel/VitalMail">View Demo</a>
    ·
    <a href="https://github.com/LeoMeinel/VitalMail/issues">Report Bug</a>
    ·
    <a href="https://github.com/LeoMeinel/VitalMail/issues">Request Feature</a>
  </p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#description">Description</a></li>
        <li><a href="#features">Features</a></li>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#commands-and-permissions">Commands and Permissions</a></li>
	  </ul>
	</li>
	<li>
	  <a href="#configuration">Configuration</a>
      <ul>
        <li><a href="#configyml">config.yml</a></li>
		<li><a href="#messagesyml">messages.yml</a></li>
      </ul>
    </li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

### Description

VitalMail is a Plugin that gives players the ability to write mail to players.

This plugin is perfect for any server wanting their players to be able to mail offline players.

### Features

* Send mail

### Built With

* [Gradle 7](https://docs.gradle.org/7.4/release-notes.html)
* [OpenJDK 17](https://openjdk.java.net/projects/jdk/17/)

<!-- GETTING STARTED -->

## Getting Started

To get the plugin running on your server follow these simple steps.

### Commands and Permissions

1. Permission: `vitalmail.send`

* Command: `/vitalmail send <player> <mail>`
* Description: Send mail

2. Permission: `vitalmail.read`

* Command: `/vitalmail read`
* Description: Read mail

3. Permission: `vitalmail.clear`

* Command: `/vitalmail clear`
* Description: Clear inbox

4. Permission: `vitalmail.cooldown.bypass`

* Description: Bypass cooldown

## Configuration

### config.yml

```
# Command delay
cooldown:
  enabled: true
  # time in s
  time: 60

# Choose a storage system (mysql or yaml)
storage-system: yaml

mysql:
  host: "localhost"
  port: 3306
  database: vitalhome
  username: "vitalhome"
  password: ""
  prefix: "server_"
```

### messages.yml

```
cmd: "&fUsage: &b/home <name> &for &b/sethome <name>"
no-perms: "&cYou don't have enough permissions!"
player-only: "&cThis command can only be executed by players!"
invalid-player: "&cInvalid player!"
same-player: "&cYou can't send mail to yourself!"
no-mail: "&cYou don't have any mail!"
inbox-full: "&cThe specified inbox is full!"
mail-sent: "&fMail has been sent"
mail-received: "&b%player% &fhas sent you a mail"
new-mail: "&f&l-> &r&fYou have new mail.\nUse &b/mail read &for &b/mail clear"
mail-cleared: "&fInbox has been cleared"
invalid-word: "&cOnly a max of &b16 alphanumeric chars/punctuation &cper word is allowed!"
invalid-mail: "&cOnly a max of &b64 chars &cper mail is allowed!"
cooldown-active: "&cYou can't use that command for another &b%time-left% &cseconds!"
```

<!-- ROADMAP -->

## Roadmap

See the [open issues](https://github.com/LeoMeinel/VitalMail/issues) for a list of proposed features (and known
issues).

<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to be, learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<!-- LICENSE -->

## License

Distributed under the GNU General Public License v3.0. See `LICENSE` for more information.

<!-- CONTACT -->

## Contact

Leopold Meinel - [@TamrielN](https://twitter.com/TamrielN) - Twitter

Leopold Meinel - [contact@tamriel.me](mailto:contact@tamriel.me) - eMail

Project Link - [VitalMail](https://github.com/LeoMeinel/VitalMail) - GitHub

<!-- ACKNOWLEDGEMENTS -->

### Acknowledgements

* [README.md - othneildrew](https://github.com/othneildrew/Best-README-Template)

<!-- MARKDOWN LINKS & IMAGES -->

[contributors-shield]: https://img.shields.io/github/contributors-anon/LeoMeinel/VitalMail?style=for-the-badge

[contributors-url]: https://github.com/LeoMeinel/VitalMail/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/LeoMeinel/VitalMail?label=Forks&style=for-the-badge

[forks-url]: https://github.com/LeoMeinel/VitalMail/network/members

[stars-shield]: https://img.shields.io/github/stars/LeoMeinel/VitalMail?style=for-the-badge

[stars-url]: https://github.com/LeoMeinel/VitalMail/stargazers

[issues-shield]: https://img.shields.io/github/issues/LeoMeinel/VitalMail?style=for-the-badge

[issues-url]: https://github.com/LeoMeinel/VitalMail/issues

[license-shield]: https://img.shields.io/github/license/LeoMeinel/VitalMail?style=for-the-badge

[license-url]: https://github.com/LeoMeinel/VitalMail/blob/main/LICENSE

[quality-shield]: https://img.shields.io/codefactor/grade/github/LeoMeinel/VitalMail?style=for-the-badge

[quality-url]: https://www.codefactor.io/repository/github/LeoMeinel/VitalMail
