import com.k2.Util.Version.Increment;
import com.k2.Util.Version.Version;

public class VersionExample {

	public static void main(String[] args) {
		
		{
			Version example = Version.create();
		
			System.out.println("The default version is: "+example);
		}
		
		{
			Version example = Version.create(1,2,3);
		
			System.out.println("This is a specific version: "+example);
		}

		{
			Version example = Version.create(1,2,3);
		
			System.out.println("This is the initial version: "+example);
			
			example.increment(Increment.POINT);
			
			System.out.println("The point version has been incremented: "+example);
			
			example.increment(Increment.MINOR);
			
			System.out.println("The minor version has been incremented: "+example);
			
			example.increment(Increment.MAJOR);
			
			System.out.println("The major version has been incremented: "+example);
			
		}
		
		{
			Version example = Version.create(1,2,3);
			Version v114 = Version.create(1,1,4);
			Version v131 = Version.create(1,3,1);
			
			if (example.includes(v114)) System.out.println("Version "+example+" contains version "+v114);
			
			if (!example.includes(v131)) System.out.println("Version "+example+" does not contain version "+v131);
		
			
		}

	}

}
