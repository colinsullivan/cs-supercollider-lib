PerformanceEnvironment : Object {

  var 
    /**
     *  Dictionary storing all loaded modules indexed by name.
     **/
    <>modules;

  *new {
    ^super.new.init();
  }

  /**
   *  Override this with the modules you want to load.
   **/
  modules_to_load {
  }

  init {
    var modulesToLoad;

    this.modules = ();

    this.load_modules(this.modules_to_load());
  }

  load_modules {
    arg modulesToLoad;
    var me = this;

    modulesToLoad.keysValuesDo({
      arg moduleName, moduleProperties;
      var module, initParams;

      initParams = moduleProperties['initParams'];

      initParams['origin'] = 0@0;
      initParams['init_done_callback'] = {
        me.modules[moduleName] = module;

        if (me.modules.size == modulesToLoad.size, {
          me.modules_all_loaded();
        });
      };
      
      ("  * Loading" + moduleName ++ "...").postln;

      module = moduleProperties['class'].new(initParams);
      
      ("  * Loaded.").postln();
      ("  * Output channel:" + initParams[\outputBus]).postln();
      ("----------------------------------------").postln;
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


    /*this.modules['clockEnvironment'].request_tempo_update();*/
  }
}
