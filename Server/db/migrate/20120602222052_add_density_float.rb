class AddDensityFloat < ActiveRecord::Migration
	
  def up
  	add_column :draws, :xdensity, :float, :default => 0
  	add_column :draws, :ydensity, :float, :default => 0
  	remove_column :draws, :resolution
  end

  def down
  end
end
