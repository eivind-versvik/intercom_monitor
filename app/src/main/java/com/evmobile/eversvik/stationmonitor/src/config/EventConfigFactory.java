package com.evmobile.eversvik.stationmonitor.src.config;

import android.graphics.Color;

import com.evmobile.eversvik.stationmonitor.src.device.Gpi;
import com.evmobile.eversvik.stationmonitor.src.device.Gpo;
import com.evmobile.eversvik.stationmonitor.src.event.action.ActionGpo;

import java.util.UUID;

/**
 * Created by eversvik on 07.04.2016.
 */

public class EventConfigFactory
{
    // Create "Door open for X seconds with display text"
    public static EventRuleConfig createTimeoutGpo(String gpioid, int timeout, String textClear, String textSet, String title)
    {
        EventRuleConfig rcfg = new EventRuleConfig(EventRuleConfig.MainType.ACTION, EventRuleConfig.SubType.TIMEOUT_GPO, gpioid, title);

        String timerid = UUID.randomUUID().toString();
        {
            EventItemConfig configclear = new EventItemConfig();
            configclear.condition = createCondGpo(gpioid, Gpo.State.clear); // WHEN GPO IS CLEARED =>
            configclear.rpc.add(createDisplay(textClear, Color.WHITE)); // EDIT DISPLAY TEXT/COLOR
            rcfg.addItem(configclear);
        }

        {
            EventItemConfig configset = new EventItemConfig();
            configset.condition = createCondGpo(gpioid, Gpo.State.set); // WHEN GPO IS SET =>
            configset.rpc.add(createDisplay(textSet, Color.WHITE)); // EDIT DISPLAY TEXT/COLOR
            rcfg.addItem(configset);
        }

        {
            EventItemConfig setWithTimeout = new EventItemConfig();
            setWithTimeout.condition = createCondClick(); // ON CLICK =>
            setWithTimeout.rpc.add(createGpo(gpioid, ActionGpo.Operation.SET)); // SET GPO
            setWithTimeout.rpc.add(createTimeout(timerid, timeout)); // START TIMER
            rcfg.addItem(setWithTimeout);
        }

        {
            EventItemConfig onTimeout = new EventItemConfig();
            onTimeout.condition = createCondTimeout(timerid); // ON TIMEOUT =>
            onTimeout.rpc.add(createGpo(gpioid, ActionGpo.Operation.CLEAR)); // CLEAR GPO
            rcfg.addItem(onTimeout);
        }

        return rcfg;
    }

    // Create toggling of gpo
    public static EventRuleConfig createToggleGpo(String gpioid, String textClear, String textSet, String title)
    {
        EventRuleConfig rcfg = new EventRuleConfig(EventRuleConfig.MainType.ACTION, EventRuleConfig.SubType.TOGGLE_GPO, gpioid, title);

        {
            EventItemConfig configclear = new EventItemConfig();
            configclear.condition = createCondGpo(gpioid, Gpo.State.clear); // WHEN GPO IS CLEARED =>
            configclear.rpc.add(createDisplay(textClear, Color.WHITE)); // EDIT DISPLAY TEXT/COLOR
            rcfg.addItem(configclear);
        }

        {
            EventItemConfig configset = new EventItemConfig();
            configset.rpc.add(createDisplay(textSet, Color.WHITE)); // WHEN GPO IS SET =>
            configset.condition = createCondGpo(gpioid, Gpo.State.set); // EDIT DISPLAY TEXT/COLOR
            rcfg.addItem(configset);
        }

        {
            EventItemConfig toggle = new EventItemConfig();
            toggle.condition = createCondClick(); // WHEN CLICKED =>
            toggle.rpc.add(createGpo(gpioid, ActionGpo.Operation.TOGGLE)); // TOGGLE GPO
            rcfg.addItem(toggle);
        }

        return rcfg;
    }

    public static EventRuleConfig createRuleNotifyGpo(String id, Gpo.State type, String body, String title) {
        EventRuleConfig rcfg = new EventRuleConfig(EventRuleConfig.MainType.NOTIFICATION, EventRuleConfig.SubType.NOTIFY_GPO, id, title);
        {
            if(body.length() > 0)
            {
                EventItemConfig set = new EventItemConfig();
                set.condition = createCondGpo(id, type);
                set.rpc.add(createNotify(title, body));
                set.rpc.add(createDisplay(body, Color.WHITE));
                rcfg.addItem(set);
            }
        }
        {

            EventItemConfig clear = new EventItemConfig();
            clear.condition = createCondGpo(id, type);
            clear.condition.gpo.setInverted();
            clear.rpc.add(createDisplay("", Color.WHITE));
            rcfg.addItem(clear);
        }

        return rcfg;
    }

    public static EventRuleConfig createRuleNotifyGpi(String id, Gpi.State type, String body, String title) {
        EventRuleConfig rcfg = new EventRuleConfig(EventRuleConfig.MainType.NOTIFICATION, EventRuleConfig.SubType.NOTIFY_GPI, id, title);
        {
            if(body.length() > 0)
            {
                EventItemConfig set = new EventItemConfig();
                set.condition = createCondGpi(id, type);
                set.rpc.add(createNotify(title, body));
                set.rpc.add(createDisplay(body, Color.WHITE));
                rcfg.addItem(set);
            }
        }
        {
            EventItemConfig clear = new EventItemConfig();
            clear.condition = createCondGpi(id, type);
            clear.condition.gpi.setInverted();
            clear.rpc.add(createDisplay("", Color.WHITE));
            rcfg.addItem(clear);
        }

        return rcfg;
    }

    private static CondConfig createCondClick()
    {
        CondConfig condcfg = new CondConfig();
        condcfg.click = new CondClickConfig();
        return condcfg;
    }

    public static CondConfig createCondGpo(String gpioid, Gpo.State state)
    {
        CondConfig condcfg = new CondConfig();
        condcfg.gpo = new CondGpoConfig(gpioid, state);
        return condcfg;
    }

    public static CondConfig createCondGpi(String gpiid, Gpi.State state)
    {
        CondConfig confcfg = new CondConfig();
        confcfg.gpi = new CondGpiConfig(gpiid, state);
        return confcfg;
    }

    private static CondConfig createCondTimeout(String id)
    {
        CondConfig condcfg = new CondConfig();
        condcfg.timer = new CondTimerConfig(id);
        return condcfg;
    }

    private static ActionConfig createDisplay(String text, int color)
    {
        ActionConfig rpccfg = new ActionConfig();
        rpccfg.display = new ActionDisplayConfig(text, color);
        return rpccfg;
    }

    private static ActionConfig createGpo(String gpioid, ActionGpo.Operation op)
    {
        ActionConfig rpccfg = new ActionConfig();
        rpccfg.gpo = new ActionGpoConfig(gpioid, op);
        return rpccfg;
    }

    private static ActionConfig createTimeout(String id, int timeout)
    {
        ActionConfig rpccfg = new ActionConfig();
        rpccfg.timer = new ActionTimerConfig(id, timeout);
        return rpccfg;
    }

    private static ActionConfig createNotify(String title, String text)
    {
        ActionConfig rpccfg = new ActionConfig();
        rpccfg.notify = new ActionNotifyConfig(title, text);
        return rpccfg;
    }


}
