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

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.sun.management.OperatingSystemMXBean;

import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

public final class SystemProperties extends JavaPlugin {
    
    @Override
    public void onEnable()
    {
        NumberFormat nf = NumberFormat.getInstance();
        
        getLogger().info("------------------------------------------------------------");
        Properties pro = System.getProperties();
        Map<Object,Object> sortedMap = new TreeMap<>(pro);
        for (Object obj : sortedMap.keySet())
        {
            getLogger().info("System." + (String)obj + ": " + System.getProperty((String)obj));
        }
        
        // OSHI
        
        try
        {
            SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer hardware = systemInfo.getHardware();
            // CPU
            CentralProcessor cpu = hardware.getProcessor();
            getLogger().info("ProcessorIdentifier.name: " + cpu.getProcessorIdentifier().getName());
            getLogger().info("CentralProcessor.PhysicalPackages: " + cpu.getPhysicalPackageCount());
            getLogger().info("CentralProcessor.PhysicalProcessors: " + cpu.getPhysicalProcessorCount());
            getLogger().info("CentralProcessor.LogicalProcessors: " + cpu.getLogicalProcessorCount());
            // Memory
            GlobalMemory memory = hardware.getMemory();
            getLogger().info("GlobalMemory.total: " + nf.format(memory.getTotal()));
            // Computer system
            ComputerSystem computer = hardware.getComputerSystem();
            getLogger().info("ComputerSystem.manufacturer: " + computer.getManufacturer());
            if (!computer.getModel().equals("unknown"))
            {
                getLogger().info("ComputerSystem.model: " + computer.getModel());
            }
            // Motherboard
            Baseboard baseboard = computer.getBaseboard();
            getLogger().info("Motherboard.manufacturer: " + baseboard.getManufacturer());
            if (!baseboard.getModel().equals("unknown"))
            {
                getLogger().info("Motherboard.model: " + baseboard.getModel());
            }
            // Disks
            List<HWDiskStore> disks = hardware.getDiskStores();
            for (int i=0; i<disks.size(); i++)
            {
                String index = (disks.size() == 1) ? "" : "_" + i;
                getLogger().info("HWDiskStore.name" + index + ": " + disks.get(i).getName());
                if (!disks.get(i).getModel().equals("unknown"))
                {
                    getLogger().info("HWDiskStore.model" + index + ": " + disks.get(i).getModel());
                }
                getLogger().info("HWDiskStore.size" + index + ": " + nf.format(disks.get(i).getSize()));
            }
            // Networks
            List<NetworkIF> networks = hardware.getNetworkIFs();
            for (int i=0; i<networks.size(); i++)
            {
                String index = (networks.size() == 1) ? "" : "_" + i;
                getLogger().info("NetworkIF.name" + index + ": " + networks.get(i).getName());
                if (!networks.get(i).getDisplayName().equals(networks.get(i).getName()))
                {
                    getLogger().info("NetworkIF.displayname" + index + ": " + networks.get(i).getDisplayName());
                }
                if (networks.get(i).getSpeed() > 0)
                {
                    getLogger().info("NetworkIF.speed" + index + ": " + nf.format(networks.get(i).getSpeed()));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        OperatingSystemMXBean os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        getLogger().info("OperatingSystemMXBean.totalMemorySize: " + nf.format(os.getTotalMemorySize()));
        getLogger().info("Runtime.availableProcessors: " + Runtime.getRuntime().availableProcessors());
        getLogger().info("Runtime.freeMemory: " + nf.format(Runtime.getRuntime().freeMemory()));
        getLogger().info("Runtime.maxMemory: " + nf.format(Runtime.getRuntime().maxMemory()));
        getLogger().info("Runtime.totalMemory: " + nf.format(Runtime.getRuntime().totalMemory()));
        
        for (MemoryPoolMXBean memoryMXBean : ManagementFactory.getMemoryPoolMXBeans())
        {
            if ("Metaspace".equals(memoryMXBean.getName()))
            {
                long maxMetaspace = memoryMXBean.getUsage().getMax();
                if (maxMetaspace >= 0)
                {
                    getLogger().info("MemoryPoolMXBean.metaspace.max: " + maxMetaspace);
                }
                break;
            }
        }
        
        for(GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans())
        {
            getLogger().info("GarbageCollectorMXBean.name: " + gc.getName());
        }
        
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        getLogger().info("RuntimeMXBean.inputArguments: ");
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
