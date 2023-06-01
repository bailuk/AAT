# 1. Add file to resource

```bash
cp /usr/share/icons/HighContrast/scalable/actions/go-first.svg icons/scalable/actions/go-first.svg
```

# 2. Update app.xml

```bash
# Generate lines for app.xml
find icons/ -type f -printf '<file preprocess="xml-stripblanks">%p</file>\n'
```

# 3. Update config/Icons.kt 

```bash
# Generate lines for Icons.kt
find icons/ -type f -printf '%f\n' | xargs -L 1 ./icons-line.sh
```
