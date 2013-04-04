PerformanceEnvironment : Object {

  var 
    /**
     *  Dictionary storing all loaded modules indexed by name.
     **/
    <>modules;

  *new {
    ^super.new.init();
  }

  init {
    var modulesToLoad;

    this.modules = ();

    modulesToLoad = (
      clockEnvironment: (
        class: ClockEnvironment
      ),
      randomizedLazersEnvironment: (
        class: RandomizedLazersEnvironment,
        outputBus: 10
      ),
      runningWaterEnvironment: (
        class: RunningWaterEnvironment,
        outputBus: 18
      ),
      randomHarpEnvironment: (
        class: RandomHarpSamplerEnvironment,
        outputBus: 14
      ),
     smoothbassEnvironment: (
        class: SmoothBassVoicerEnvironment,
        outputBus: 12
      ),
      fmPercussionEnvironment: (
        class: FMPercussionVoicerEnvironment,
        outputBus: 16
      ),
      granularChaosEnvironment: (
        class: GranularChaosEnvironment,
        outputBus: 20
      )
    );

    this.load_modules(modulesToLoad);
  }

  load_modules {
    arg modulesToLoad;
    var me = this;

    modulesToLoad.keysValuesDo({
      arg moduleName, moduleProperties;
      var module;
      
      module = moduleProperties['class'].new((
        outputBus: moduleProperties['outputBus'],
        origin: 0@0,
        init_done_callback: {
          me.modules[moduleName] = module;

          if (me.modules.size == modulesToLoad.size, {
            me.modules_all_loaded();    
          });
        }
      ));
    });
  }

  modules_all_loaded {
    var screenHeight = 960,
      screenWidth = 1440,
      instrX,
      instrY;
    
    instrY = screenHeight - 34;
    instrX = 0;

    // layout windows
    this.modules.keysValuesDo({
      arg moduleName, module;
      
      instrY = instrY - module.window.bounds.height;

      module.window.bounds.moveTo(instrX@instrY);
    });


    this.modules['clockEnvironment'].request_tempo_update();
  }
}
