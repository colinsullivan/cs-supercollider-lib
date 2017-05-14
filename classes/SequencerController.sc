SequencerController : Object {
  var store,
    sequencers;
  *new {
    arg params;

    ^super.new.init(params);
  }

  init {
    arg params;
    var state,
      sequencersState;
    store = params['store'];

    state = store.getState();
    sequencersState = state['sequencers'];

    sequencers = IdentityDictionary.new();

    // create sequencers
    sequencersState.keysValuesDo({
      arg id, sequencerState;
      var sequencerClass = sequencerState['class'].asSymbol().asClass();
      sequencers.put(id, sequencerClass.new((store: store, sequencerId: id)));
    })

    //"sequencersState:".postln;
    //sequencersState.postln;


    //sequencer = GenerativeSequencer.new((
      //store: store,
      //stateAddress: "sequencers.metro"
    //));

    
  }
}
