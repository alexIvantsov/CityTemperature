package com.example.citytemperature.dagger

import com.example.citytemperature.CityTemperature
import dagger.Component
import javax.inject.Singleton
import dagger.android.support.AndroidSupportInjectionModule
import dagger.BindsInstance


@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class
    ]
)
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: CityTemperature): Builder

        fun build(): AppComponent
    }

    fun inject(app: CityTemperature)
}