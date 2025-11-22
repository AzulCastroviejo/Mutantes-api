Resultados de Tests - Proyecto Mutantes



- Proyecto
![](C:\Users\pcast\OneDrive\Escritorio\Imágenes\Capturas de pantalla\Captura de pantalla 2025-11-15 231902.png)

- Service
  ![](C:\Users\pcast\OneDrive\Escritorio\Imágenes\Capturas de pantalla\Captura de pantalla 2025-11-15 231852.png)


---

## Comandos para Reproducir

### Ejecutar todos los tests
```bash
./gradlew test
```

### Generar reporte de cobertura
```bash
./gradlew jacocoTestReport
```

### Ver reportes
```bash
# Reporte de tests
start build\reports\tests\test\index.html

# Reporte de cobertura
start build\reports\jacoco\test\html\index.html
```

---

## Ubicación de los Reportes

- **Tests:** `build/reports/tests/test/index.html`
- **Cobertura:** `build/reports/jacoco/test/html/index.html`

---

## Conclusión

El proyecto cumple con **todos los requisitos de testing**:
- ✅ Más de 80% de cobertura
- ✅ 35 tests automatizados
- ✅ Tests unitarios y de integración
- ✅ Cobertura del algoritmo principal >95%
- ✅ Todos los tests pasan exitosamente

---

## Capturas de Render funcional
Link de render :  https://mutantes-api-1.onrender.com

![](C:\Users\pcast\OneDrive\Escritorio\Imágenes\Capturas de pantalla\Captura de pantalla 2025-11-16 175948.png)

Capturas de Swagger 

- Con render prueba exitosa de OK 200
![](C:\Users\pcast\OneDrive\Escritorio\Imágenes\Capturas de pantalla\Captura de pantalla 2025-11-14 125229.png)

- Prueba error 403
![](C:\Users\pcast\OneDrive\Escritorio\Imágenes\Capturas de pantalla\Captura de pantalla 2025-11-14 125302.png)

---

## Capturas de Swagger funional desde el local

- OK 200
![](C:\Users\pcast\OneDrive\Escritorio\Imágenes\Capturas de pantalla\Captura de pantalla 2025-11-16 122429.png)

- Error 403
![](C:\Users\pcast\OneDrive\Escritorio\Imágenes\Capturas de pantalla\Captura de pantalla 2025-11-16 122600.png)

## Capturas de H2 funcional

![](C:\Users\pcast\OneDrive\Escritorio\Imágenes\Capturas de pantalla\Captura de pantalla 2025-11-16 122816.png)

**Fecha de generación:** 18/11/25
**Autor:** Azul Castroviejo