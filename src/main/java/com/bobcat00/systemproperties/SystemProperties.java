// SystemProperties - Displays selected Java runtime parameters
// Copyright 2019 Bobcat00
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package com.bobcat00.systemproperties;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.bukkit.plugin.java.JavaPlugin;

public final class SystemProperties extends JavaPlugin {
    
    @Override
    public void onEnable()
    {
        getLogger().info("------------------------------------------------------------");
        Properties pro = System.getProperties();
        Map<Object,Object> sortedMap = new TreeMap<>(pro);
        for (Object obj : sortedMap.keySet())
        {
            getLogger().info("System." + (String)obj + ": " + System.getProperty((String)obj));
        }
        
        getLogger().info("Runtime.availableProcessors: " + Runtime.getRuntime().availableProcessors());
        getLogger().info("Runtime.freeMemory: " + Runtime.getRuntime().freeMemory());
        getLogger().info("Runtime.maxMemory: " + Runtime.getRuntime().maxMemory());
        getLogger().info("Runtime.totalMemory: " + Runtime.getRuntime().totalMemory());
        
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        getLogger().info("RuntimeMXBean.InputArguments: ");
        for (String str : arguments)
        {
            getLogger().info("  " + str);
        }
        getLogger().info("------------------------------------------------------------");
    }
 
    @Override
    public void onDisable()
    {
    }
}
