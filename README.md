# Sharedr opener (Proof of concept)

Android 12 takes away the possibility of opening a custom share sheet from a third party app. Only the system share sheet can be used.  
Apps like Sharedr cannot be used in that case.  

This project attempts to make use of Accessibility service to auto-select Sharedr from system share menu, this making the experience less of a pain for users.  
It is not the best way out, but it is better than nothing.  

Things which can be adjusted:
1. <b>Constants.kt</b>: `SHAREDR_LABEL` can be changed with any other package name by any other app.  
2. <b>AppAccessibilityService.kt</b>: `sharePackageNames` is a list of package names of inbuilt system share sheets, which triggers opening Sharedr. More names can be added there.  