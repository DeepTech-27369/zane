# Git Setup for DeepTech-27369/zane

Our repo: https://github.com/DeepTech-27369/zane.git

The git commands are the same on macOS and Windows. Only installing git and opening a terminal differ.

---

## 1. Install Git

**Windows**
1. Download and install from https://git-scm.com/download/win (the default options are fine).
2. Open **Git Bash** or **PowerShell**.

**macOS**
1. Open **Terminal** (Cmd + Space, then type "Terminal").
2. Run this command, and click **Install** if a popup appears:
   ```
   xcode-select --install
   ```

**Both:** check that it worked:
```
git --version
```

## 2. Tell Git who you are (one time per computer)

```
git config --global user.name "Your Name"
git config --global user.email "you@example.com"
```

---

## 3a. Get the code (new computer / new teammate)

```
git clone https://github.com/DeepTech-27369/zane.git
cd zane
git remote add upstream https://github.com/FIRST-Tech-Challenge/FtcRobotController.git
```

Then in Android Studio use **File > Open** and select the `zane` folder.

## 3b. Attach an existing FtcRobotController copy to our repo

This is how the repo was first set up. Only use it if you already have a
copy of FtcRobotController that was cloned from FIRST.

```
cd path/to/FtcRobotController
git remote rename origin upstream
git remote add origin https://github.com/DeepTech-27369/zane.git
git push -u origin master
```

- `origin` is our team repo, where we push.
- `upstream` is FIRST's official repo, where we pull SDK updates.

Check the remotes with:
```
git remote -v
```

---

## 4. Everyday workflow

```
git pull                         # get teammates' latest changes FIRST
# ... edit code in Android Studio ...
git status                       # see what changed
git add .                        # stage all changes
git commit -m "Describe what you changed"
git push                         # upload to GitHub
```

## 4b. Same thing using Android Studio's built-in Git

Android Studio uses the same Git setup, so it pushes to our repo just like the commands do.

| Command | Android Studio (Windows / macOS shortcut) |
|---|---|
| `git pull` | **Git > Pull** (Ctrl+T / Cmd+T) |
| `git add` + `git commit` | **Git > Commit** (Ctrl+K / Cmd+K). Tick the files, write a message, then click **Commit** |
| `git push` | **Git > Push** (Ctrl+Shift+K / Cmd+Shift+K) |
| Commit and push in one step | In the Commit window, click **Commit and Push** |
| `git pull upstream master` | **Git > Pull**, then pick the `upstream` remote and the `master` branch |

When you push, check that the Push window shows **master → origin : master**.
The first time you push, choose **Log in via GitHub** if Android Studio asks you to sign in.

## 5. Update the FTC SDK (when FIRST releases a new version)

```
git pull upstream master
git push
```

If git reports merge conflicts, ask a mentor before you continue.

---

## Signing in when you push

- **Windows:** a browser window opens so you can sign in to GitHub. Sign in, and Git remembers you.
- **macOS:** if Git asks for a password, enter a **Personal Access Token**, not your
  GitHub password. To create one, go to GitHub > Settings > Developer settings > Personal access tokens.
  You can also install GitHub CLI (`brew install gh`) and run `gh auth login`.

You need write access to `DeepTech-27369/zane`. Ask the org owner to add you.

---

## Troubleshooting: "Permission denied" / error 403 when pushing from Android Studio

Example error:
```
remote: Permission to DeepTech-27369/zane.git denied to <your-username>.
fatal: unable to access 'https://github.com/DeepTech-27369/zane.git/': The requested URL returned error: 403
```

**Why it happens:** Android Studio uses its own GitHub login instead of the one the terminal uses.
Our GitHub organization can block that login even when your account has access.

**Quick workaround:** open the **Terminal** tab at the bottom of Android Studio and run:
```
git push
```

**Permanent fixes.** Try them in order and stop when one works:

1. **Make Android Studio use the terminal's login**
   - Go to **Settings > Version Control > Git** (on macOS, **Android Studio > Settings**) and untick **Use credential helper**.
   - If you don't see that option, go to **Settings > Version Control > GitHub**, remove your account, and push again.
     When it asks you to sign in, choose the **Git credentials / password** option, not "Log in via GitHub".

2. **Approve Android Studio for the organization**
   - On github.com, click your avatar, then go to **Settings > Applications > Authorized OAuth Apps > JetBrains IDE Integration**.
   - Next to `DeepTech-27369`, click **Request**, or **Grant** if you are an org owner.
   - An org owner then approves it under **Organization Settings > Third-party access > OAuth application policy**.

3. **Log in with a token**
   - On github.com, go to **Settings > Developer settings > Personal access tokens > Tokens (classic) > Generate new token**.
   - Tick the **`repo`** scope, generate the token, and copy it.
   - In Android Studio, go to **Settings > Version Control > GitHub**, remove your account, click **+**, then **Log in with Token...**, and paste the token.
   - If the org uses SSO, click **Configure SSO** next to the token on GitHub and authorize it for `DeepTech-27369`.

If none of these work, ask an org owner to check that you have **Write** access to the `zane` repo
(Repo **Settings > Collaborators and teams**).
