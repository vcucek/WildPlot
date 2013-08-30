package si.wildplot.core.render;

import java.util.HashSet;
import si.wildplot.common.util.SpecialParameter;
import si.wildplot.core.DrawContext;
import si.wildplot.core.WildPlotObjectImpl;

/*
 * (C) Copyright 2013 Vito Čuček.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * @author Vito Čuček <vito.cucek@xlab.si>
 */
public abstract class AbstractPlot extends WildPlotObjectImpl implements Function{

	private final int numParam;
	protected String function;
	protected HashSet<SpecialParameter> specialParametersList = new HashSet<SpecialParameter>();

	protected boolean enabled = true;

	public AbstractPlot(String function, int numParam){
		this.function = function;
		this.numParam = numParam;
		parseSpecialParameters(numParam);
	}

	public String getFunction()
	{
		return this.function;
	}

	public void setFunction(String function){
		this.function = function;
		parseSpecialParameters(numParam);
	}

	public boolean isEnabled()
	{
		return this.enabled;
	}

	public void setEnabled(boolean enable)
	{
		this.enabled = enable;
	}

	private void parseSpecialParameters(int numParam){
		this.specialParametersList.clear();
		int counter = 0;
		char[] func = function.replaceAll(" ", "").toCharArray();
		for(char c : func){
			boolean isFuncParam = false;
			if(numParam == 1){isFuncParam = (c == 'x') ? true : false;}
			if(numParam == 2){isFuncParam = (c == 'x' || c == 'y') ? true : false;}
			if(numParam == 3){isFuncParam = (c == 'x' || c == 'y' || c == 'z') ? true : false;}
			if(String.valueOf(c).matches("[A-Za-z]") && !isFuncParam){
				if(counter==0 && func.length > 1){
					char nexC = func[counter+1];
					if(String.valueOf(nexC).matches("[^A-Za-z0-9]")){
						specialParametersList.add(new SpecialParameter(c));
					}

				}
				else if(counter==func.length - 1 && func.length > 1){
					char prevC = func[counter-1];
					if(String.valueOf(prevC).matches("[^A-Za-z0-9]")){
						specialParametersList.add(new SpecialParameter(c));
					}

				}
				else if(func.length > 3){
					char prevC = func[counter-1];
					char nexC = func[counter+1];
					if(String.valueOf(prevC).matches("[^A-Za-z0-9]") && String.valueOf(nexC).matches("[^A-Za-z0-9]")){
						specialParametersList.add(new SpecialParameter(c));
					}
				}
			}
			counter += 1;
		}
	}

	public SpecialParameter getSpecialParameter(char name){
		for(SpecialParameter sp : this.specialParametersList){
			if(sp.name == name){
				return sp;
			}
		}
		return null;
	}

	public SpecialParameter[] getSpecialParameters(){
		SpecialParameter[] array = new SpecialParameter[this.specialParametersList.size()];
		return specialParametersList.toArray(array);
	}

	public void render(DrawContext dc)
	{
		if(!this.enabled){
			return;
		}
		if(function.equalsIgnoreCase("") || this.function == null){
			return;
		}
		doRender(dc);
	}

	public abstract void doRender(DrawContext dc);
}
