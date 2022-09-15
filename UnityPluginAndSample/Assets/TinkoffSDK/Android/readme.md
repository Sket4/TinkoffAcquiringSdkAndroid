#Для начала работы:
для загрузки зависимостей добавьте плагин ExternalDependencyManager
1. Добавьте правила proguard в файл proguard-user, который сгенерирован Unity. Правила описаны в файле proguard-user-example
2. Активируйте кастомный файл AndroidManifest.xml и добавьте туда activity: <activity android:name="com.tzargames.tinkoff_unity.TinkoffPaymentActivity" android:theme="@style/Theme.AppCompat.Light"></activity> (рядом с главным activity, не внутри него)
3. Добавить в AndroidManifest в секцию application (и не забыть заменить packagename на свой): <provider android:name="androidx.startup.InitializationProvider" android:authorities="com.DefaultCompany.TinkoffSDK_test.androidx-startup" tools:node="remove" />
3. Создайте экземпляр класса TinkoffPaymentParams и вызовите его статический метод StartPaymentProcess