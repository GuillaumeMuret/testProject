/**
 * @file TestManager.java
 * @brief proxy of the TestManager.
 * @version 1.0
 * @date 14/04/2016
 * @author Guillaume Muret
 * @copyright Copyright (c) 2016, PRØVE
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of PRØVE, Angers nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY PRØVE AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL PRØVE AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package fr.prove.thingy.model.proxy;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.prove.thingy.bus.BusProtocol;
import fr.prove.thingy.communication.Postman;
import fr.prove.thingy.communication.protocole.ProcessOut;
import fr.prove.thingy.communication.protocole.ProtocoleVocabulary;
import fr.prove.thingy.communication.protocole.NameProcessOut;

public class TestManager extends ProxyModel {

    /**
     * Constructor of the TestManager
     * @param postman : the postman
     */
    public TestManager(Postman postman) {
        super(postman);
    }

    /**
     * encryption of the owner, function, params to create a Json String.
     * @param procedureOut : the process to call
     * @param data : the data
     * @return the json object convert into string
     */
    @Override
    protected String toJSONString(ProcessOut procedureOut, Bundle data) {

        JSONObject mainObj = new JSONObject();
        JSONObject params = new JSONObject();
        try {
            mainObj.put(ProtocoleVocabulary.JSON_OWNER, procedureOut.owner);
            mainObj.put(ProtocoleVocabulary.JSON_NUMOWNER, procedureOut.numOwner);
            mainObj.put(ProtocoleVocabulary.JSON_PROCESS, procedureOut.process);
            switch (procedureOut.process) {

                case NameProcessOut.TEST_MANAGER_SET_CAMPAIGN:

                    ArrayList<String> ids = data.getStringArrayList(BusProtocol.BUS_DATA_TEST_MANAGER_ID_SCENARIOS);
                    ArrayList<Integer> run = data.getIntegerArrayList(BusProtocol.BUS_DATA_TEST_MANAGER_SHOULD_RUN);

                    if (ids != null && run != null && ids.size() == run.size()) {

                        JSONArray items = new JSONArray();
                        for (int i = 0; i < ids.size(); i++) {
                            JSONObject single = new JSONObject();
                            single.put(ProtocoleVocabulary.JSON_PARAMS_ID_SCENARIO, ids.get(i));
                            single.put(ProtocoleVocabulary.JSON_PARAMS_RUN, run.get(i));
                            items.put(single);
                        }

                        params.put(ProtocoleVocabulary.JSON_PARAMS_ITEMS, items);

                    } else {
                        Log.e("TestManager", "Error : ID scenarios and run arrays nulls or not the same size");
                    }

                    break;

                case NameProcessOut.TEST_MANAGER_SET_STOP:
                    params.put(ProtocoleVocabulary.JSON_PARAMS_STATE, data.getInt(BusProtocol.BUS_DATA_TEST_MANAGER_STOP));
                    break;
            }
            mainObj.put(ProtocoleVocabulary.JSON_PARAMS, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mainObj.toString();

    }
}