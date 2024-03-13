FROM ibm-semeru-runtimes:open-21.0.2_13-jre-jammy

COPY --chown=1001:0 target/jakarta-data-openliberty.jar jakarta-data-openliberty.jar

EXPOSE 9080

ENTRYPOINT ["java", "-jar", "jakarta-data-openliberty.jar"]