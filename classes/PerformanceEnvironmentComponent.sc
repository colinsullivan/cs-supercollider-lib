/**
 *  @file       PerformanceEnvironmentComponent.sc
 *
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *  @copyright  2017 Colin Sullivan
 *  @license    Licensed under the GPLv3 license.
 **/

/**
 *  @class  PerformanceEnvironmentComponent   A component of a performance
 *  environment that has a GUI window.
 **/
PerformanceEnvironmentComponent : Object {
  var 
    <params,
    <>origin,
    <>uc33Controller,
    <>softStepController,
    <>abletonController,
    <>launchControlController,
    <>sixteennController,
    <>pc12Controller,
    <>pc4Controller,
    <>interface,
    <window,
    initDoneCallback,
    // number
    <>outputBus,
    <playing,
    // id of component for looking up in state store
    <>componentId,
    componentState,
    // save state of controllerMappings and rerun mapping method when changed
    controllerMappings,
    <store,
    <clock;

  *new {
    arg inParams;

    ^super.new.init(inParams);
  }

  getComponentStatePath {
    ^("components." ++ componentId);
  }
  getComponentStatePathArray {
    ^["components", componentId];
  }

  getComponentState {
    var state = store.getState();

    ^state.components[componentId];
  }

  init {
    arg inParams;
    var me = this,
      state;

    params = inParams;

    componentId = params['componentId'];

    store = params['store'];
    clock = params['clock'];

    if ((componentId != nil), {
      componentState = this.getComponentState();
      store.subscribe({
        this.handle_state_change();
      });
    });

    playing = false;

    if (componentState != nil, {
      if (componentState.outputBus != nil, {
        this.outputBus = componentState.outputBus;
      });
    }, {
      if (params['outputBus'] == nil, {
        this.outputBus = 0;
      }, {
        this.outputBus = params['outputBus'];
      });
    });

    
    this.origin = params['origin'];
    initDoneCallback = params['initDoneCallback'];
 
    this.load_samples({
      interface = this.create_interface({
        this.load_environment();
        this.init_external_controller_mappings();
      });
      this.load_gui({
        this.play();
        initDoneCallback.value();
      });
    });
  }

  play {
    interface.play();
  }

  load_gui {
    arg onDone;
    interface.gui = {
      arg layout, metaPatch;
      this.init_gui((
        window: layout.parent.parent,
        layout: layout,
        metaPatch: metaPatch
      ));
    };
    AppClock.sched(0.0, {
      interface.gui();
      onDone.value();
    });
  }

  handle_state_change {
    var prevComponentState = componentState;
    componentState = this.getComponentState();
    if (controllerMappings != componentState.controllerMappings, {
      controllerMappings = componentState.controllerMappings;
      this.init_external_controller_mappings();
    });
  }

  create_interface {
    arg onDone;
    var interface = Interface({
      onDone.value();
    }).onPlay_({
      this.on_play();
    }).onStop_({
      this.on_stop();
    });

    ^interface;
  }
  
  /**
   *  Load all audio buffers required for this component.
   *  
   *  @param  Function  callback  To call when done loading samples.
   **/
  load_samples {
    arg callback;
    // subclasses should load samples before using callback
    callback.value();
  }

  init_patches {
  
  }

  load_environment {
    this.init_patches(params);
  }


  init_gui {
    arg params;

    window = params['window'];

    if (origin != nil, {
      window.bounds = window.bounds.moveTo(this.origin[0], this.origin[1]);
    });
    window.name = this.gui_window_title();
  }
 
  /**
   *  Called when the play button is pressed on the interface.
   **/
  on_play {
    playing = true;
  }

  /**
   *  Called when the stop button is pressed on the interface.
   **/
  on_stop {
    playing = false;
  }

  // TODO: Move instantiation of controllers out of here
  init_external_controller_mappings {
    var uc33Port,
      softStepPort,
      launchControlPort,
      abletonPort;
    
    //uc33Port = MIDIIn.findPort("UC-33 USB MIDI Controller", "Port 1");

    //if (uc33Port != nil, {
      //// sub-classes should use this UC33Ktl instance to assign knobs and such.
      //this.uc33Controller = UC33Ktl.new(
        //uc33Port.uid
      //);

      //this.init_uc33_mappings();
    //}, {
      //// sub-classes should check to see if uc33Controller is nil to determine
      //// if it is currently connected.
      //this.uc33Controller = nil;
    //});

    //softStepPort = MIDIIn.findPort("SoftStep Share", "SoftStep Share");
    
    //if (softStepPort != nil, {
      //this.softStepController = SoftStepKtl.new(softStepPort.uid);
      //this.init_softStep_mappings();
    //}, {
      //this.softStepController = nil;
    //});
    
    //abletonPort = MIDIIn.findPort("(out) SuperCollider", "(out) SuperCollider");
    //if (abletonPort != nil, {
      //this.abletonController = AbletonKtl.new(abletonPort.uid);
      //this.init_ableton_mappings();
    //}, {
      //this.abletonController = nil;
    //});

    
    //MIDIIn.findPort("Launch Control XL", "Launch Control XL");
    this.launchControlController = MKtl(
      'CSLaunchControlXL',
      "novation-launchcontrol-xl",
      multiIndex: 0
    );

    this.sixteennController = MKtl(
      'CS16n',
      "16n"
    );

    this.pc12Controller = MKtl(
      'CSPC12',
      "faderfox-pc12"
    );
    this.pc4Controller = MKtl(
      'CSPC4',
      "faderfox-pc4"
    );
    //MKtl(
      //'CSLaunchControlXL',
      //"novation-launchcontrol-xl",
      //multiIndex: 0
    //);

    //MKtl('CSLaunchControlXL').trace(true);
    //"hasDevice: ".postln();
    //MKtl('CSLaunchControlXL').hasDevice.postln();
    //MKtl('CSLaunchControlXL').openDevice


    if (controllerMappings != nil, {
        controllerMappings.keysValuesDo({
          arg controllerName, mappings;
          var controller;

          controller = this.performMsg([controllerName]);
          if (controller != nil, {
            mappings.keysValuesDo({
              arg controlName, property;
              ("mapping `" ++ controlName ++ "` to this." ++ property).postln();
              this.map_controller_to_property(
                controller,
                controlName.asSymbol(),
                property.asSymbol()
              );
            });
          },
          {
            ("No controller named:  " + controllerName).postln();
          });

        });
    });



    this.init_launchcontrol_mappings();
    //if (launchControlPort != nil, {
      ////this.launchControlController = LaunchControlXLKtl.new(launchControlPort.uid);
    //}, {
      //this.launchControlController = nil;
    //});
  }

  /**
   *  Initialize mappings for specific controllers if they are present.
   **/
  init_uc33_mappings {
  
  }

  init_softStep_mappings {
  
  }

  init_ableton_mappings {
  
  }

  init_launchcontrol_mappings {

  }

  /**
   *  Returns the member variables at the provided keys.
   *
   *  @private
   *
   *  @param  {Symbol|Array}  propertyKeys -  The keys at which to get the
   *          properties.  i.e. this.<something>
   *  @param  {Object}  from - The object to get properties from.  Optional,
   *          defaults to `this`.
   **/
  pr_get_properties_from_keys {
    arg propertyKeys, from = this;
    var properties;

    if (propertyKeys.class == Symbol, {
      propertyKeys = [propertyKeys];    
    });

    properties = Array.new(propertyKeys.size());

    propertyKeys.do({
      arg propertyKey;

      properties = properties.add(from.performMsg([propertyKey]));
    });

    ^properties;
  
  }

  /**
   *  Map a property of the component (member variable) to any controller
   *  knob or slider.
   *
   *  @param  {MKtlDevice}  controller - The controller instance.
   *  @param  Symbol  elementKey - The key used to identify the knob or slider on the 
   *  controller.  Ex. \sl1
   *  @param  Symbol|Array  Used as a key to identify the property of the
   *  component to control with the aforementioned controller knob.
   *  Ex: \amp.  If provided an array, knob controls all of the elements.
   *  @param  [Object]  The actual object to map to.  Defaults to `this` for
   *  mapping controller to member variables.
   **/
  map_controller_to_property {
    arg controller, elementKey, propertyKeys, mapTo = this;
    var properties, controllerElement;

    properties = this.pr_get_properties_from_keys(propertyKeys, mapTo);

    controllerElement = controller.dictAt(elementKey);
    if (controllerElement == nil, {
      (
        "No element `"
        ++ elementKey
        ++ "` not found in controller `"
        ++ controller
        ++ "`"
      ).postln();
    }, {
      controllerElement.addAction({
        arg element;
        properties.do({
          arg property;

          property.set(property.spec.map(element.value));
        });
      });
    });
  }

  /**
   *  Map a property of the component (member variable) to a UC-33 controller
   *  knob or slider.
   *
   *  @param  Symbol  The key used to identify the knob or slider on the 
   *  controller.  Ex. \sl1
   *  @param  Symbol|Array  Used as a key to identify the property of the
   *  component to control with the aforementioned controller knob.
   *  Ex: \amp.  If provided an array, knob controls all of the elements.
   *  @param  [Object]  The actual object to map to.  Defaults to `this` for
   *  mapping controller to member variables.
   **/
  map_uc33_to_property {
    arg controllerComponent, propertyKeys, mapTo = this;
    var properties;

    properties = this.pr_get_properties_from_keys(propertyKeys, mapTo);

    this.uc33Controller.mapCCS(1, controllerComponent, {
      arg ccval;

      properties.do({
        arg property;

        property.set(property.spec.map(ccval / 127));
      });

    });
  }

  /**
   *  Map an outgoing MIDI control from Ableton to a property on this
   *  component.
   *
   *  @param  {Number}  channel - MIDI channel
   *  @param  {Number}  cc - CC (as listed on the ableton interface)
   *  @param  {Symbol|Array}  propertyKeys - The property we will assign these
   *          cc messages to.
   *  @param  {Object}  mapTo - Optional
   **/
  map_ableton_cc_to_property {
    arg channel, cc, propertyKeys, mapTo = this;
    var properties, abletonCCKey;

    properties = this.pr_get_properties_from_keys(propertyKeys, mapTo);

    abletonCCKey = this.abletonController.makeCCKey(channel, cc);

    this.abletonController.mapCC(abletonCCKey, {
      arg ccval;

      properties.do({
        arg property;

        property.set(property.spec.map(ccval / 127));
      });
    });
  }

  /**
   *  Called to set title of GUI window.  Default is the name of the class
   *  of the component.
   **/
  gui_window_title {
    ^this.class.asString();
  }
}
